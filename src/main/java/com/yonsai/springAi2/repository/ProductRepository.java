package com.yonsai.springAi2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yonsai.springAi2.entity.Product;

public interface ProductRepository extends
					JpaRepository<Product, Long>{

}
