package com.sparta.java_02.domain.product.repository;

import com.sparta.java_02.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
  List<Product> findByIdIn(List<Long> ids);
}
