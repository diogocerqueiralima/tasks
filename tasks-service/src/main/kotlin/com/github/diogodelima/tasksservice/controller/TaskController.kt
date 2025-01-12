package com.github.diogodelima.tasksservice.controller

import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.dto.ApiResponseDto
import com.github.diogodelima.tasksservice.dto.TaskCreateDto
import com.github.diogodelima.tasksservice.dto.TaskDto
import com.github.diogodelima.tasksservice.dto.toDto
import com.github.diogodelima.tasksservice.services.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(

    private val taskService: TaskService

) {

    @PostMapping
    fun create(@RequestBody @Valid dto: TaskCreateDto, @RequestHeader("User-Id") userId: Int): ResponseEntity<ApiResponseDto<TaskDto>> {

        val task = taskService.create(
            title = dto.title,
            description = dto.description,
            deadline = dto.deadline,
            creatorId = userId
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

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable("id") id: Int, @RequestHeader("User-Id") userId: Int): ResponseEntity<ApiResponseDto<TaskDto>> {

        val task = taskService.getTaskById(id, userId)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Task retrieved successfully",
                    data = task.toDto()
                )
            )
    }

    @GetMapping
    fun getTasks(@RequestHeader("User-Id") userId: Int): ResponseEntity<ApiResponseDto<List<TaskDto>>> =
        ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Tasks retrieved successfully",
                    data = taskService.getTasks(userId).map { it.toDto() }
                )
            )

}