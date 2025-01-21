package com.github.diogodelima.tasksservice.dto

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.serializer.DateSerializer
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TaskUpdateDto(

    @field:NotNull
    val id: Int,

    val title: String? = null,

    val description: String? = null,

    @Serializable(with = DateSerializer::class)
    val deadline: LocalDateTime? = null,

    val tags: List<Task.Tag>? = null

)