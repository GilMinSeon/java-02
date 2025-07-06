package com.sparta.java_02.domain.product.controller;

import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.product.dto.ProductResponse;
import com.sparta.java_02.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ApiResponse<List<ProductResponse>> getAllProducts() {
    return ApiResponse.success(productService.getAllProducts());
  }
}
