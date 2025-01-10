package com.github.diogodelima.tasksservice.repositories

import com.github.diogodelima.tasksservice.domain.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Int>