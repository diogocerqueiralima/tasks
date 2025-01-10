package com.github.diogodelima.tasksservice.exceptions.handler

import com.github.diogodelima.tasksservice.dto.ApiResponseDto
import com.github.diogodelima.tasksservice.exceptions.StepNotFoundException
import com.github.diogodelima.tasksservice.exceptions.TaskAccessDeniedException
import com.github.diogodelima.tasksservice.exceptions.TaskNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(TaskNotFoundException::class, StepNotFoundException::class)
    fun handleNotFound(e: Exception): ResponseEntity<ApiResponseDto<Any>> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ApiResponseDto(message = e.message ?: "")
            )

    @ExceptionHandler(TaskAccessDeniedException::class)
    fun handleForbidden(e: Exception): ResponseEntity<ApiResponseDto<Any>> =
        ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                ApiResponseDto(message = e.message ?: "")
            )

}