package com.github.diogodelima.tasksservice.domain

import jakarta.persistence.*

@Entity
@Table(name = "steps")
data class Step(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Task.Status = Task.Status.PENDING,

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    val task: Task

)