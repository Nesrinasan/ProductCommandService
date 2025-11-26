package com.backendguru.productService.controller;

import com.backendguru.productService.dto.ProductSaveRequestDto;
import com.backendguru.productService.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public void save(@RequestBody ProductSaveRequestDto productSaveRequestDto){

        productService.saveProduct(productSaveRequestDto);

    }


}
