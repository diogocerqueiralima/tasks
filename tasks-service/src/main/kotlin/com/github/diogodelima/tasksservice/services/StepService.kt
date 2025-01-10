package com.github.diogodelima.tasksservice.services

import com.github.diogodelima.tasksservice.domain.Step
import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.exceptions.StepNotFoundException
import com.github.diogodelima.tasksservice.exceptions.TaskAccessDeniedException
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

    fun update(stepId: Int, userId: Int, status: Task.Status): Step {

        val step = stepRepository.findById(stepId).orElseThrow { StepNotFoundException() }

        if (step.task.creatorId != userId)
            throw TaskAccessDeniedException()

        return stepRepository.save(step.copy(status = status))
    }

}