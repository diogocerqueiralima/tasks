package com.github.diogodelima.tasksservice.services

import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.exceptions.TaskAccessDeniedException
import com.github.diogodelima.tasksservice.exceptions.TaskNotFoundException
import com.github.diogodelima.tasksservice.repositories.TaskRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class TaskServiceTest {

    @Mock
    private lateinit var taskRepository: TaskRepository

    @InjectMocks
    private lateinit var taskService: TaskService

    @Test
    fun `create task should succeed`() {

        val expected = Task(title = "Limpar a Casa", description = "Aspirar, lavar o chão e tirar o pó da casa", deadline = LocalDateTime.now(), creatorId = 1)

        Mockito.`when`(taskRepository.save(Mockito.any(Task::class.java)))
            .thenReturn(expected)

        val actual = taskService.create(expected.title, expected.description, expected.deadline, expected.creatorId)

        assertEquals(expected, actual)
    }

    @Test
    fun `get task by id that does not exist should fail`() {

        assertThrows<TaskNotFoundException> {
             taskService.getTaskById(1, 1)
        }

    }

    @Test
    fun `get a task by id that does not belong to the user, must fail`() {

        val task = Task(title = "Limpar a Casa", description = "Aspirar, lavar o chão e tirar o pó da casa", deadline = LocalDateTime.now(), creatorId = 1)

        Mockito.`when`(taskRepository.findById(1))
            .thenReturn(Optional.of(task))

        assertThrows<TaskAccessDeniedException> {
            taskService.getTaskById(1, 2)
        }

    }

    @Test
    fun `get task by id should succeed`() {

        val expected = Task(title = "Limpar a Casa", description = "Aspirar, lavar o chão e tirar o pó da casa", deadline = LocalDateTime.now(), creatorId = 1)

        Mockito.`when`(taskRepository.findById(1))
            .thenReturn(Optional.of(expected))

        val actual = taskService.getTaskById(1, 1)

        assertEquals(expected, actual)
    }

}