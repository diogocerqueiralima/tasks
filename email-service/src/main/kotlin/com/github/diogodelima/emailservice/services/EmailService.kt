package com.github.diogodelima.emailservice.services

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service


@Service
class EmailService {

    @KafkaListener(topics = ["forgot-password"], groupId = "email-service")
    fun requestEmailToResetPasswordProcess(msg: String) {
        println("Venda recebida: $msg")
    }

}