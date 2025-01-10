package com.github.diogodelima.tasksservice.dto

import com.github.diogodelima.tasksservice.domain.Task
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
data class StepUpdateDto(

    @field:NotNull
    val status: Task.Status

)