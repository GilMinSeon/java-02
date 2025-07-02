package com.sparta.java_02.domain.purchase.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PurchaseProductResponse {

    Long productId;

    String productName;

    Integer quantity;

    BigDecimal price;

    BigDecimal totalPrice;
}
