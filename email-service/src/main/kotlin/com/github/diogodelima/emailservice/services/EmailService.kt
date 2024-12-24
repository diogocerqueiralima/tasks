package com.github.diogodelima.emailservice.services

import com.github.diogodelima.emailservice.dto.UserForgotPasswordDto
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context


@Service
class EmailService(

    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine

) {

    @KafkaListener(topics = ["forgot-password"], groupId = "email-service")
    fun requestEmailToResetPasswordProcess(dto: UserForgotPasswordDto) {

        val context = Context()

        context.setVariable("username", dto.username)
        context.setVariable("url", dto.url)

        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)
        val htmlContent: String = templateEngine.process(dto.template, context)

        helper.setTo(dto.email)
        helper.setSubject(dto.subject)
        helper.setText(htmlContent, true)

        mailSender.send(mimeMessage)
    }

}