package com.backendguru.productService.repository;

import com.backendguru.productService.model.AuditLog;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {
}
