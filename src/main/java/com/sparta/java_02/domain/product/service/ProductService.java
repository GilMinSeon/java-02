package com.sparta.java_02.domain.product.service;

import com.sparta.java_02.domain.product.dto.ProductResponse;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream()
        .map((product -> ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .categoryId(product.getCategory().getId())
            .createdAt(product.getCreatedAt())
            .build())).toList(); // 엔티티를 response객체 형태로 매핑해서 리턴
  }
}
