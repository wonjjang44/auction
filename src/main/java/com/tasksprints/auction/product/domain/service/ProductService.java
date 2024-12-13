package com.tasksprints.auction.product.domain.service;

import com.tasksprints.auction.product.domain.dto.request.ProductRequest;
import com.tasksprints.auction.product.domain.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * item으로 Auction 검색
 */
public interface ProductService {
    String uploadImage(MultipartFile image) throws IOException;

    List<String> uploadImageBulk(List<MultipartFile> images);

    List<ProductResponse> getProductsByUserId(Long userId);

    ProductResponse getProductByAuctionId(Long auctionId);

    ProductResponse register(Long userId, Long auctionId, ProductRequest.Register product, List<MultipartFile> images);

    ProductResponse update(ProductRequest.Update product);
}
