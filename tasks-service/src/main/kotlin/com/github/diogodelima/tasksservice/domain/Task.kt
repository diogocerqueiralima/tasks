package com.github.diogodelima.tasksservice.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val deadline: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val creatorId: Int,

    @OneToMany(fetch = FetchType.EAGER, cascade = [(CascadeType.ALL)], mappedBy = "task", orphanRemoval = true)
    val steps: List<Step> = emptyList()

) {

    val status: Status
        get() = when {
            steps.all { it.status == Status.COMPLETED } -> Status.COMPLETED
            steps.any { it.status == Status.IN_PROGRESS } -> Status.IN_PROGRESS
            else -> Status.PENDING
        }

    enum class Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

}
