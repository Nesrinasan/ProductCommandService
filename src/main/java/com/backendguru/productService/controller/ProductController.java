package com.backendguru.productService.controller;

import com.backendguru.productService.dto.ProductSaveRequestDto;
import com.backendguru.productService.dto.ProductUpdateRequestDto;
import com.backendguru.productService.service.ProductService;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/updateName")
    public void updateName(@RequestBody ProductUpdateRequestDto productUpdateRequestDto){
        productService.updateName(productUpdateRequestDto);

    }


    @PutMapping("/updateNameAllProduct")
    public void updateNameAllProduct() {
        productService.updateNameAllProduct();
    }

}
