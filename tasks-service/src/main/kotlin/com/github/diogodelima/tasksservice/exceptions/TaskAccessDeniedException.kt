package com.github.diogodelima.tasksservice.exceptions

class TaskAccessDeniedException(

    override val message: String? = "Task access denied"

) : RuntimeException(message)