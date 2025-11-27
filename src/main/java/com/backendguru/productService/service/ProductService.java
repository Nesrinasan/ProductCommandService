package com.backendguru.productService.service;

import com.backendguru.productService.dto.ProductCreatedEvent;
import com.backendguru.productService.dto.ProductSaveRequestDto;
import com.backendguru.productService.dto.ProductUpdateEvent;
import com.backendguru.productService.dto.ProductUpdateRequestDto;
import com.backendguru.productService.model.Product;
import com.backendguru.productService.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

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
        product.setName(productSaveRequestDto.getName() + " "+ new Random().nextInt(10));
        productRepository.save(product);
        auditLogService.logProductCreation(product.getName());
        ProductCreatedEvent productUpdateEvent = new ProductCreatedEvent(product.getId().toString(), product.getName(), product.getCategory(), product.getPrice());

        String productStr = null;
        try {
            productStr = objectMapper.writeValueAsString(productUpdateEvent);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaMessageProducer.sendMessage(productStr);

    }

    @Transactional
    public void updateName(ProductUpdateRequestDto productUpdateRequestDto){

        Long id = productUpdateRequestDto.getId();
        Product product = productRepository.findById(id).get();
        product.setName(productUpdateRequestDto.getName());
        product = productRepository.save(product);
        ProductUpdateEvent productUpdateEvent = new ProductUpdateEvent(product.getId().toString(), product.getName());

        String productStr = null;
        try {
            productStr = objectMapper.writeValueAsString(productUpdateEvent);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaMessageProducer.sendUpdateProductMessage(productStr, product.getId().toString());

    }


    @Transactional
    public void updateNameAllProduct() {
        Iterable<Product> products = productRepository.findAll();
        products.forEach(product -> {
            String name = product.getName() + new Random().nextInt(10000);
            product.setName(name);
            productRepository.save(product);
            try {
                ProductUpdateEvent productUpdateNameReqestDto = new ProductUpdateEvent(product.getId().toString(), name);
                String productCreateEventStr = objectMapper.writeValueAsString(productUpdateNameReqestDto);
                kafkaMessageProducer.sendUpdateProductMessage(productCreateEventStr, product.getId().toString());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });


    }

}
