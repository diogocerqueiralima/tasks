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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status = Status.PENDING,

    @OneToMany(fetch = FetchType.EAGER, cascade = [(CascadeType.ALL)], mappedBy = "task", orphanRemoval = true)
    val steps: List<Step> = emptyList()

) {

    enum class Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

}
