package com.sparta.java_02.domain.category.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.category.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final Jedis jedis;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final CategoryRepository categoryRepository;

  private static final String CACHE_KEY_CATEGORY_STRUCT = "categoryStruct";
  private static final int CACHE_EXPIRE_SECONDS = 3600; // 1시간

  public List<CategoryResponse> getCategoryStruct() {
    List<Category> categories = categoryRepository.findAll();
    Map<Long, CategoryResponse> categoryResponseMap = new HashMap<>();

    for (Category category : categories) {
      CategoryResponse response = CategoryResponse.builder()
          .id(category.getId())
          .name(category.getName())
          .categories(new ArrayList<>())
          .build();
      categoryResponseMap.put(category.getId(), response);
    }

    List<CategoryResponse> rootCategories = new ArrayList<>();
    for (Category category : categories) {
      CategoryResponse categoryResponse = categoryResponseMap.get(category.getId());

      if (ObjectUtils.isEmpty(category.getParent())) {
        rootCategories.add(categoryResponse);
      } else {
        CategoryResponse parentCategoryResponse = categoryResponseMap.get(
            category.getParent().getId());
        if (ObjectUtils.isEmpty(parentCategoryResponse)) {
          continue;
        }
        parentCategoryResponse.getCategories().add(categoryResponse);
      }
    }

    return rootCategories;
  }


  @Transactional(readOnly = true)
  public List<CategoryResponse> findCategoryStructCacheAside() {
    String cachedCategories = jedis.get(CACHE_KEY_CATEGORY_STRUCT);

    try {
      if (StringUtils.hasText(cachedCategories)) {
        return objectMapper.readValue(cachedCategories,
            new TypeReference<List<CategoryResponse>>() {
            });
      }

      List<CategoryResponse> categories = getCategoryStruct();
      String jsonString = objectMapper.writeValueAsString(categories);
      jedis.set(CACHE_KEY_CATEGORY_STRUCT, jsonString);

      return categories;

    } catch (Exception e) {
      throw new RuntimeException("JSON 파싱 버그");
    }
  }


  @Transactional
  public void saveWriteThrough(CategoryRequest request) {
    Category parentCategory = ObjectUtils.isEmpty(request)
        ? null
        : categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

    Category category = Category.builder()
        .name(request.getName())
        .parent(parentCategory)
        .build();

    categoryRepository.save(category); // 저장 빠진 것 같아 추가

    try {
      List<CategoryResponse> rootCategories = getCategoryStruct();

      if (!ObjectUtils.isEmpty(rootCategories)) {
        String jsonString = objectMapper.writeValueAsString(rootCategories);
        jedis.set(CACHE_KEY_CATEGORY_STRUCT, jsonString);
      }

    } catch (Exception e) {
      //log.error("Error updating cache key {}: {}", CACHE_KEY_CATEGORY_STRUCT, e.getMessage());
    }

  }
}
