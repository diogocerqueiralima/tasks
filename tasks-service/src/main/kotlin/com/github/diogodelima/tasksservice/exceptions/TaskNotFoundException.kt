package com.github.diogodelima.tasksservice.exceptions

class TaskNotFoundException(

    override val message: String? = "Task not found"

) : RuntimeException(message)