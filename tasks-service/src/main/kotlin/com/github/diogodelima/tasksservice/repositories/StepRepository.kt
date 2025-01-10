package com.github.diogodelima.tasksservice.repositories

import com.github.diogodelima.tasksservice.domain.Step
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StepRepository : JpaRepository<Step, Int>