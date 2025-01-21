package com.github.diogodelima.tasksservice.dto

import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TaskDto(

    val id: Int,

    val title: String,

    val description: String,

    @SerialName("created_at")
    @Serializable(with = DateSerializer::class)
    val createdAt: LocalDateTime,

    @Serializable(with = DateSerializer::class)
    val deadline: LocalDateTime,

    val steps: List<Int>,

    val status: Task.Status,

    val tags: List<Task.Tag>

)

fun Task.toDto() = TaskDto(
    id = this.id,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt,
    deadline = this.deadline,
    steps = this.steps.map { it.id },
    status = this.status,
    tags = this.tags
)