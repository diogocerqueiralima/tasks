package com.github.diogodelima.tasksservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TasksServiceApplication

fun main(args: Array<String>) {
    runApplication<TasksServiceApplication>(*args)
}
