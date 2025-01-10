package com.github.diogodelima.tasksservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StepCreateDto(

    @field:NotBlank
    val name: String,

    @SerialName("task_id")
    @field:NotNull
    val taskId: Int

)