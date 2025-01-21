package com.github.diogodelima.tasksservice.dto

import com.github.diogodelima.tasksservice.domain.Task
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class TaskCreateDto(

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val description: String,

    @field:NotNull
    val deadline: LocalDateTime,

    @field:NotEmpty
    val tags: List<Task.Tag>

)