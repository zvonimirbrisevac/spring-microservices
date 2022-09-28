package com.microservicestutorial.productservice.services;

import com.microservicestutorial.productservice.DTOs.ProductRequestDTO;
import com.microservicestutorial.productservice.DTOs.ProductResponseDTO;
import com.microservicestutorial.productservice.models.Product;
import com.microservicestutorial.productservice.repos.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepo productRepository;

    public void createProduct(ProductRequestDTO productRequest) {
        Product product = new Product(productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice());

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponseDTO mapToProductResponse(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
