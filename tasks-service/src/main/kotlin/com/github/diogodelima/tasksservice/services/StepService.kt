package com.github.diogodelima.tasksservice.services

import com.github.diogodelima.tasksservice.domain.Step
import com.github.diogodelima.tasksservice.repositories.StepRepository
import org.springframework.stereotype.Service

@Service
class StepService(

    private val stepRepository: StepRepository,
    private val taskService: TaskService

) {

    fun create(name: String, taskId: Int, userId: Int): Step {

        val task = taskService.getTaskById(taskId, userId)

        return stepRepository.save(
            Step(
                name = name,
                task = task
            )
        )
    }

}