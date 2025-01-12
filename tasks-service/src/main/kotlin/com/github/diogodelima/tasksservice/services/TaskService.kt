package com.github.diogodelima.tasksservice.services

import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.exceptions.TaskAccessDeniedException
import com.github.diogodelima.tasksservice.exceptions.TaskNotFoundException
import com.github.diogodelima.tasksservice.repositories.TaskRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaskService(

    private val taskRepository: TaskRepository

) {

    fun create(title: String, description: String, deadline: LocalDateTime, creatorId: Int): Task =
        taskRepository.save(
            Task(
                title = title,
                description = description,
                deadline = deadline,
                creatorId = creatorId
            )
        )

    fun getTaskById(id: Int, userId: Int): Task {

        val task = taskRepository.findById(id).orElseThrow { TaskNotFoundException() }

        if (task.creatorId != userId)
            throw TaskAccessDeniedException()

        return task
    }

    fun getTasks(userId: Int) =
        taskRepository.findTasksByCreatorId(userId)

}