package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.constant.Constants;
import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductResponse;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseCancelService {

  private final UserRepository userRepository;
  private final PurchaseRepository purchaseRepository;
  private final PurchaseProductRepository purchaseProductRepository;

  @Transactional
  public PurchaseCancelResponse cancelPurchase(Long purchaseId, Long userId) {

    // 설명: purchaseRepository => db에서 구매정보를 가져오는 객체
    // findByIdAndUser_Id => Purchase 엔티티에서 id가 purchaseId이고, 그 안에 있는 user.id가 userId인 걸 찾아줘.
    // 다른 사람의 구매를 취소하려는 경우를 막기 위해서
    Purchase purchase = purchaseRepository.findByIdAndUser_Id(purchaseId, userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

    validatePurchaseStatus(purchase);

    // JPA의 Dirty Checking 기능으로, 트랜잭션 커밋 시 자동 업데이트됨
    purchase.setStatus(PurchaseStatus.CANCELED);

    //구매 ID로 해당 구매에 포함된 모든 상품을 조회해 옴
    List<PurchaseProduct> purchaseProducts = purchaseProductRepository.findByPurchase_Id(
        purchase.getId());

    restoreProductStock(purchaseProducts);

    // 응답용 DTO
    List<PurchaseProductResponse> purchaseProductResponses = getPurchaseProductResponses(
        purchaseProducts);

    return PurchaseCancelResponse.builder()
        .purchaseId(purchase.getId())
        .purchaseStatus(purchase.getStatus())
        .cancelledAt(LocalDateTime.now())
        .message(Constants.PURCHASE_CANCEL_MESSAGE)
        .cancelledProducts(purchaseProductResponses)
        .build();
  }

  // 상태가 대기중이 아니라면 취소 불가
  private void validatePurchaseStatus(Purchase purchase) {
    if (purchase.getStatus() != PurchaseStatus.PENDING) {
      throw new ServiceException(ServiceExceptionCode.CANNOT_CANCEL);
    }
  }

  private void restoreProductStock(List<PurchaseProduct> purchaseProducts) {
    for (PurchaseProduct purchaseProduct : purchaseProducts) {
      Product product = purchaseProduct.getProduct();
      product.increaseStock(purchaseProduct.getQuantity());
    }
  }

  private List<PurchaseProductResponse> getPurchaseProductResponses(
      List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map((purchaseProduct) -> {
          Product product = purchaseProduct.getProduct();
          BigDecimal totalPrice = purchaseProduct.getPrice()
              .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity()));

          return PurchaseProductResponse.builder()
              .productId(product.getId())
              .productName(product.getName())
              .quantity(purchaseProduct.getQuantity())
              .price(purchaseProduct.getPrice())
              .totalPrice(totalPrice)
              .build();
        }).toList();
  }
}


