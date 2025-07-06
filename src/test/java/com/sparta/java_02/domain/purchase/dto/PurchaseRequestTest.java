package com.sparta.java_02.domain.purchase.dto;

import java.util.List;

public class PurchaseRequestTest {

  Long userId;

  List<PurchaseProductRequestTest> purchaseProducts;

  public PurchaseRequestTest(Long userId, List<PurchaseProductRequestTest> purchaseProducts) {
    this.purchaseProducts = purchaseProducts;
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<PurchaseProductRequestTest> getPurchaseProducts() {
    return purchaseProducts;
  }

  public void setPurchaseProducts(
      List<PurchaseProductRequestTest> purchaseProducts) {
    this.purchaseProducts = purchaseProducts;
  }
}
