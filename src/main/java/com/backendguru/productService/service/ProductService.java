package com.backendguru.productService.service;

import com.backendguru.productService.dto.ProductSaveRequestDto;
import com.backendguru.productService.model.Product;
import com.backendguru.productService.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final AuditLogService auditLogService;

    private final ModelMapper modelMapper;

    private final KafkaMessageProducer kafkaMessageProducer;

    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, AuditLogService auditLogService, ModelMapper modelMapper, KafkaMessageProducer kafkaMessageProducer, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.auditLogService = auditLogService;
        this.modelMapper = modelMapper;
        this.kafkaMessageProducer = kafkaMessageProducer;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void saveProduct(ProductSaveRequestDto productSaveRequestDto){
        Product product = modelMapper.map(productSaveRequestDto, Product.class);
        productRepository.save(product);
        auditLogService.logProductCreation(product.getName());

        String productStr = null;
        try {
            productStr = objectMapper.writeValueAsString(product);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaMessageProducer.sendMessage(productStr);

    }


}
