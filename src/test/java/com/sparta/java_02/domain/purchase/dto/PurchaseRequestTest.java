package com.sparta.java_02.domain.purchase.dto;

import java.util.List;

public class PurchaseRequestTest {

  Long userId;

  List<PurchaseProductRequestTest> products;

  public PurchaseRequestTest(Long userId, List<PurchaseProductRequestTest> products) {
    this.products = products;
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<PurchaseProductRequestTest> getPurchaseProducts() {
    return products;
  }

  public void setPurchaseProducts(
      List<PurchaseProductRequestTest> products) {
    this.products = products;
  }
}
