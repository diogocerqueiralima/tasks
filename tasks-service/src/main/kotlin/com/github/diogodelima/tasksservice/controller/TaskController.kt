package com.github.diogodelima.tasksservice.controller

import com.github.diogodelima.tasksservice.dto.*
import com.github.diogodelima.tasksservice.services.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
@CrossOrigin
class TaskController(

    private val taskService: TaskService

) {

    @PostMapping
    fun create(@RequestBody @Valid dto: TaskCreateDto): ResponseEntity<ApiResponseDto<TaskDto>> {

        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userId = jwt.getClaimAsString("user_id")

        val task = taskService.create(
            title = dto.title,
            description = dto.description,
            deadline = dto.deadline,
            tags = dto.tags,
            creatorId = userId.toInt()
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponseDto(
                    message = "Task created successfully",
                    data = task.toDto()
                )
            )
    }

    @PutMapping("/{id}")
    fun update(@RequestBody @Valid dto: TaskUpdateDto): ResponseEntity<ApiResponseDto<TaskDto>> {

        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userId = jwt.getClaimAsString("user_id")

        val task = taskService.update(
            id = dto.id,
            userId = userId.toInt(),
            title = dto.title,
            description = dto.description,
            deadline = dto.deadline,
            tags = dto.tags
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Task updated successfully",
                    data = task.toDto()
                )
            )
    }

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable("id") id: Int): ResponseEntity<ApiResponseDto<TaskDto>> {

        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userId = jwt.getClaimAsString("user_id")
        val task = taskService.getTaskById(id, userId.toInt())

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Task retrieved successfully",
                    data = task.toDto()
                )
            )
    }

    @GetMapping
    fun getTasks(): ResponseEntity<ApiResponseDto<List<TaskDto>>> {

        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userId = jwt.getClaimAsString("user_id")

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Tasks retrieved successfully",
                    data = taskService.getTasks(userId.toInt()).map { it.toDto() }
                )
            )
    }

}