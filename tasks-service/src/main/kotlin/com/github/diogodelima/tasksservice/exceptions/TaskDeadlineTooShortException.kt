package com.github.diogodelima.tasksservice.exceptions

class TaskDeadlineTooShortException(

    override val message: String? = "Task deadline too short"

) : RuntimeException(message)