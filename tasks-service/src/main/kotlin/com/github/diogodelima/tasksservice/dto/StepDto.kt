package com.github.diogodelima.tasksservice.dto

import com.github.diogodelima.tasksservice.domain.Task
import kotlinx.serialization.Serializable

@Serializable
data class StepDto(

    val id: Int,

    val name: String,

    val status: Task.Status,

    val taskId: Int

)