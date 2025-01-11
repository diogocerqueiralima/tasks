package com.github.diogodelima.tasksservice.exceptions

class StepNotFoundException(

    override val message: String? = "Step not found"

) : RuntimeException(message)