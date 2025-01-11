package com.github.diogodelima.tasksservice.services

import com.github.diogodelima.tasksservice.domain.Step
import com.github.diogodelima.tasksservice.domain.Task
import com.github.diogodelima.tasksservice.exceptions.StepNotFoundException
import com.github.diogodelima.tasksservice.exceptions.TaskAccessDeniedException
import com.github.diogodelima.tasksservice.exceptions.TaskNotFoundException
import com.github.diogodelima.tasksservice.repositories.StepRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
class StepServiceTest {

    @Mock
    private lateinit var stepRepository: StepRepository

    @Mock
    private lateinit var taskService: TaskService

    @InjectMocks
    private lateinit var stepService: StepService

    @Test
    fun `create step with the task id that does not exists should fail`() {

        Mockito.`when`(taskService.getTaskById(1, 1))
            .thenThrow(TaskNotFoundException::class.java)

        assertThrows<TaskNotFoundException> {
            stepService.create("Lavar o chão", 1, 1)
        }

        Mockito.verify(stepRepository, Mockito.never()).save(Mockito.any())
    }

    @Test
    fun `create step with the task that does not belong to the user, must fail`() {

        Mockito.`when`(taskService.getTaskById(1, 1))
            .thenThrow(TaskAccessDeniedException::class.java)

        assertThrows<TaskAccessDeniedException> {
            stepService.create("Lavar o chão", 1, 1)
        }

        Mockito.verify(stepRepository, Mockito.never()).save(Mockito.any())
    }

    @Test
    fun `create step should succeed`() {

        val task = Task(title = "Limpar a Casa", description = "Aspirar, lavar o chão e tirar o pó da casa", deadline = LocalDateTime.now(), creatorId = 1)

        Mockito.`when`(taskService.getTaskById(1, 1))
            .thenReturn(task)

        val expected = Step(name = "Limpar a Casa", task = task)

        Mockito.`when`(stepRepository.save(Mockito.any(Step::class.java)))
            .thenReturn(expected)

        val actual = stepService.create("Limpar a Casa", 1, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `update step that does not exist should fail`() {

        Mockito.`when`(stepRepository.findById(1))
            .thenReturn(Optional.empty())

        assertThrows<StepNotFoundException> {
            stepService.update(1, 1, Task.Status.COMPLETED)
        }

        Mockito.verify(stepRepository, Mockito.never()).save(Mockito.any())
    }

    @Test
    fun `update step with the task that does not belong to the user, must fail`() {

        val task = Task(title = "Limpar a Casa", description = "Aspirar, lavar o chão e tirar o pó da casa", deadline = LocalDateTime.now(), creatorId = 2)
        val step = Step(name = "Limpar a Casa", task = task)

        Mockito.`when`(stepRepository.findById(1))
            .thenReturn(Optional.of(step))

        assertThrows<TaskAccessDeniedException> {
            stepService.update(1, 1, Task.Status.COMPLETED)
        }

        Mockito.verify(stepRepository, Mockito.never()).save(Mockito.any())
    }

    @Test
    fun `update step should succeed`() {

        val task = Task(title = "Limpar a Casa", description = "Aspirar, lavar o chão e tirar o pó da casa", deadline = LocalDateTime.now(), creatorId = 1)
        val step = Step(name = "Limpar a Casa", task = task)

        Mockito.`when`(stepRepository.findById(1))
            .thenReturn(Optional.of(step))

        val expected = step.copy(status = Task.Status.COMPLETED)

        Mockito.`when`(stepRepository.save(expected))
            .thenReturn(expected)

        val actual = stepService.update(1, 1, Task.Status.COMPLETED)

        assertEquals(expected, actual)
    }

}