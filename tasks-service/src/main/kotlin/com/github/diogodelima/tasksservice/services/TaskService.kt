package com.github.diogodelima.tasksservice.services

import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.exceptions.TaskAccessDeniedException
import com.github.diogodelima.tasksservice.exceptions.TaskDeadlineTooShortException
import com.github.diogodelima.tasksservice.exceptions.TaskNotFoundException
import com.github.diogodelima.tasksservice.repositories.TaskRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaskService(

    private val taskRepository: TaskRepository

) {

    fun create(title: String, description: String, deadline: LocalDateTime, tags: List<Task.Tag>, creatorId: Int): Task {

        if (deadline.isBefore(LocalDateTime.now().plusHours(1)))
            throw TaskDeadlineTooShortException()

        return taskRepository.save(
            Task(
                title = title,
                description = description,
                deadline = deadline,
                tags = tags,
                creatorId = creatorId
            )
        )

    }

    fun update(id: Int, userId: Int, title: String?, description: String?, deadline: LocalDateTime?, tags: List<Task.Tag>?): Task {

        val task = taskRepository.findById(id).orElseThrow { TaskNotFoundException() }

        if (task.creatorId != userId)
            throw TaskAccessDeniedException()

        deadline?.let {

            if (it.isBefore(LocalDateTime.now().plusHours(1)))
                throw TaskDeadlineTooShortException()

        }

        return taskRepository.save(
            Task(
                id = task.id,
                title = title ?: task.title,
                description = description ?: task.description,
                createdAt = task.createdAt,
                deadline = deadline ?: task.deadline,
                steps = task.steps,
                creatorId = task.creatorId,
                tags = tags ?: task.tags
            )
        )
    }

    fun getTaskById(id: Int, userId: Int): Task {

        val task = taskRepository.findById(id).orElseThrow { TaskNotFoundException() }

        if (task.creatorId != userId)
            throw TaskAccessDeniedException()

        return task
    }

    fun getTasks(userId: Int) =
        taskRepository.findTasksByCreatorId(userId)

}