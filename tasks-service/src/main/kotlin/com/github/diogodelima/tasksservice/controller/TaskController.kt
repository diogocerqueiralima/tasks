package com.github.diogodelima.tasksservice.controller

import com.github.diogodelima.tasksservice.dto.ApiResponseDto
import com.github.diogodelima.tasksservice.dto.TaskCreateDto
import com.github.diogodelima.tasksservice.dto.TaskDto
import com.github.diogodelima.tasksservice.services.TaskService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TasksController(

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
            .ok(
                ApiResponseDto(
                    message = "Task created successfully",
                    data = TaskDto(
                        id = task.id,
                        title = task.title,
                        description = task.description,
                        createdAt = task.createdAt,
                        deadline = task.deadline,
                        steps = task.steps.map { it.id },
                        status = task.status
                    )
                )
            )
    }

}