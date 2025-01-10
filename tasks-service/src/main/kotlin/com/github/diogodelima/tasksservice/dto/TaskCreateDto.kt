package com.github.diogodelima.tasksservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class TaskCreateDto(

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val description: String,

    @field:NotNull
    val deadline: LocalDateTime

)