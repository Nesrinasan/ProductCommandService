package com.backendguru.productService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topic-name}")
    private String topicName;

    public KafkaMessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message){
        CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(topicName, message);
        send.whenComplete((result, ex) -> {
            if(ex == null){
                System.out.println("mesaj başaarı ile işlendi.");
            }else {
                System.out.println("MEsaj gönderilemedi.");
            }
        });
    }

}
