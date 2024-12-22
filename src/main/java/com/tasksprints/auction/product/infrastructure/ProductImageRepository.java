package com.tasksprints.auction.product.infrastructure;

import com.tasksprints.auction.product.domain.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
