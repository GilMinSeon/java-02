package com.sparta.java_02.domain.category.controller;


import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public ApiResponse<List<CategoryResponse>> getCategories() {
    return ApiResponse.success(categoryService.getCategoryStruct());
  }

}
