package com.backendguru.productService.service;

import com.backendguru.productService.model.AuditLog;
import com.backendguru.productService.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logProductCreation( String productName) {
        AuditLog log = new AuditLog();
        log.setMessage(productName + ": ürün işleme başladı");
        auditLogRepository.save(log);

    }

}