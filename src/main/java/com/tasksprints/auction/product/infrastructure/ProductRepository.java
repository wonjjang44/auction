package com.tasksprints.auction.product.infrastructure;

import com.tasksprints.auction.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByAuctionId(Long auctionId);

    // 쿼리를 메서드 이름으로 표현
    List<Product> findByOwnerId(Long ownerId);
}
