package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final PurchaseRepository purchaseRepository;
  private final ProductRepository productRepository;
  private final PurchaseProductRepository purchaseProductRepository;
  private final UserRepository userRepository;

  private final PurchaseCancelService purchaseCancelService;

  @Transactional
  public void createPurchase(PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    // 1) purchase를 save하는 메서드 -> 특정 도메인을 세이브하는 메서드니까 뺀다
    // 해당 사용자에 대한 Purchase 객체 생성
    Purchase purchase = purchaseRepository.save(Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.ZERO)
        .status(PurchaseStatus.PENDING)
        .build());

    // 총 금액 초기화
    BigDecimal totalPrice = BigDecimal.ZERO;

    // 구매상품 리스트 생성, 반복문에서 만들어질 구매상품(PurchaseProduct)들을 저장할 리스트
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();

    // 반복문 => 구매요청된 각 상품에 대해 처리 start!!!! ===============================
    // 클라이언트에서 보낸 여러 상품 구매요청을 하나씩 꺼냄
    for (PurchaseProductRequest productRequest : request.getPurchaseProducts()) {

      // 상품 조회 + 예외 처리
      Product product = productRepository.findById(productRequest.getProductId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

      // 재고부족 확인, 구매요청수량이 상품재고보다 많으면 예외
      if (productRequest.getQuantity() > product.getStock()) {
        throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
      }

      // 재고 차감, 상품 엔티티에서 재고를 차감하는 로직, 영속상태이므로 트랜잭션 끝나면 jpa가 자동 반영
      product.reduceStock(productRequest.getQuantity());
      PurchaseProduct purchaseProduct = PurchaseProduct.builder()
          .product(product)
          .purchase(purchase)
          .quantity(productRequest.getQuantity())
          .price(product.getPrice())
          .build();
      purchaseProducts.add(purchaseProduct);

      // 총 결제금액 계산
      totalPrice = totalPrice.add(
          product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity())));

    }
    // 반복문 end!! ====================================================================

    purchase.setTotalPrice(totalPrice); // 내가 setter만듦
    purchaseProductRepository.saveAll(purchaseProducts);
  }

  /*week 3, day3 클린코드, cancelService */
  @Transactional
  public PurchaseCancelResponse cancel(PurchaseCancelRequest request) {
    // user 검증은 Auth 에서 수행 했다고 가정
    return purchaseCancelService.cancelPurchase(request.getPurchaseId(), request.getUserId());
  }
}

// 세번째 단계
// 단일책임원칙<클래스 분리>
// 마더클래스로 분리한 소스
/*

    private final PurchaseCancelService cancelService;
    private final PurchaseProcessService purchaseProcessService;
    private final UserRepository userRepository;

    @Transactional
    public void createPurchase(PurchaseRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

        purchaseProcessService.process(user, request.getPurchaseProducts());
    }

 */