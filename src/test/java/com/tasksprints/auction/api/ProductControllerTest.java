package com.tasksprints.auction.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksprints.auction.api.product.ProductController;
import com.tasksprints.auction.common.constant.ApiResponseMessages;
import com.tasksprints.auction.domain.product.dto.ProductDTO;
import com.tasksprints.auction.domain.product.dto.ProductRequest;
import com.tasksprints.auction.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("제품 등록 성공")
    @Test
    public void testRegisterProduct_Success() throws Exception {
        ProductRequest.Register productRequest = new ProductRequest.Register(); // Populate with necessary fields
        ProductDTO productDTO = new ProductDTO(); // Populate with necessary fields

        when(productService.register(anyLong(), anyLong(), any())).thenReturn(productDTO);

        mockMvc.perform(post("/api/v1/product/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.PRODUCT_NOT_FOUND)); // Adjust as needed
    }

    @DisplayName("제품 등록 실패")
    @Test
    public void testRegisterProduct_Failure() throws Exception {
        ProductRequest.Register productRequest = new ProductRequest.Register(); // Populate with necessary fields

        when(productService.register(anyLong(), anyLong(), any())).thenThrow(new RuntimeException("Error registering product"));

        mockMvc.perform(post("/api/v1/product/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error registering product"));
    }

    @DisplayName("userId를 통해 제품 조회 성공")
    @Test
    public void testGetProductsByUserId_Success() throws Exception {
        List<ProductDTO> products = Collections.singletonList(new ProductDTO()); // Populate with necessary fields
        when(productService.getProductsByUserId(anyLong())).thenReturn(products);

        mockMvc.perform(get("/api/v1/product/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.REVIEWS_RETRIEVED))
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("auctionId를 통해 제품 조회 성공")
    @Test
    public void testGetProductByAuctionId_Success() throws Exception {
        ProductDTO productDTO = new ProductDTO(); // Populate with necessary fields
        when(productService.getProductByAuctionId(anyLong())).thenReturn(productDTO);

        mockMvc.perform(get("/api/v1/product/auction/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.PRODUCT_NOT_FOUND)); // Adjust as needed
    }

    @DisplayName("제품 수정 성공")
    @Test
    public void testUpdateProduct_Success() throws Exception {
        ProductRequest.Update productRequest = new ProductRequest.Update(); // Populate with necessary fields
        ProductDTO updatedProductDTO = new ProductDTO(); // Populate with necessary fields
        when(productService.update(any())).thenReturn(updatedProductDTO);

        mockMvc.perform(put("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.REVIEW_RETRIEVED)); // Adjust as needed
    }

    @DisplayName("userId를 통해 제품 삭제 성공")
    @Test
    public void testDeleteProduct_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.PRODUCT_NOT_FOUND)); // Adjust as needed

        verify(productService, times(1)).delete(1L);
    }
}
