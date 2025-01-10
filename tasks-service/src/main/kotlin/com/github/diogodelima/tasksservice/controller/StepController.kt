package com.github.diogodelima.tasksservice.controller

import com.github.diogodelima.tasksservice.dto.ApiResponseDto
import com.github.diogodelima.tasksservice.dto.StepCreateDto
import com.github.diogodelima.tasksservice.dto.StepDto
import com.github.diogodelima.tasksservice.dto.StepUpdateDto
import com.github.diogodelima.tasksservice.services.StepService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/steps")
class StepController(

    private val stepService: StepService

) {

    @PostMapping
    fun create(@RequestBody @Valid dto: StepCreateDto, @RequestHeader("User-Id") userId: Int): ResponseEntity<ApiResponseDto<StepDto>> {

        val step = stepService.create(dto.name, dto.taskId, userId)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponseDto(
                    message = "Step created successfully",
                    data = StepDto(
                        id = step.id,
                        name = step.name,
                        status = step.status,
                        taskId = step.task.id
                    )
                )
            )
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody @Valid dto: StepUpdateDto, @RequestHeader("User-Id") userId: Int): ResponseEntity<ApiResponseDto<StepDto>> {

        val step = stepService.update(id, userId, dto.status)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Step updated successfully",
                    data = StepDto(
                        id = step.id,
                        name = step.name,
                        status = step.status,
                        taskId = step.task.id
                    )
                )
            )
    }

}