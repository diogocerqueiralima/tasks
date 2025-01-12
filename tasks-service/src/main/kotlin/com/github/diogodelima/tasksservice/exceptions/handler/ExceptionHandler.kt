package com.github.diogodelima.tasksservice.exceptions.handler

import com.github.diogodelima.tasksservice.dto.ApiResponseDto
import com.github.diogodelima.tasksservice.exceptions.StepNotFoundException
import com.github.diogodelima.tasksservice.exceptions.TaskAccessDeniedException
import com.github.diogodelima.tasksservice.exceptions.TaskDeadlineTooShortException
import com.github.diogodelima.tasksservice.exceptions.TaskNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(TaskDeadlineTooShortException::class)
    fun handleBadRequest(e: Exception): ResponseEntity<ApiResponseDto<Any>> =
        handleException(e, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(TaskAccessDeniedException::class)
    fun handleForbidden(e: Exception): ResponseEntity<ApiResponseDto<Any>> =
        handleException(e, HttpStatus.FORBIDDEN)

    @ExceptionHandler(TaskNotFoundException::class, StepNotFoundException::class)
    fun handleNotFound(e: Exception): ResponseEntity<ApiResponseDto<Any>> =
        handleException(e, HttpStatus.NOT_FOUND)

    private fun handleException(e: Exception, httpStatusCode: HttpStatusCode): ResponseEntity<ApiResponseDto<Any>> =
        ResponseEntity
            .status(httpStatusCode)
            .body(
                ApiResponseDto(message = e.message ?: "")
            )

}