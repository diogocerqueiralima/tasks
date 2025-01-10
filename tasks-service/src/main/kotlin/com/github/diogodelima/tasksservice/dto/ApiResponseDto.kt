package com.github.diogodelima.tasksservice.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDto<T>(

    val message: String,
    val data: T? = null

)
