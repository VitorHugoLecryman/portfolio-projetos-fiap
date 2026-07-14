package br.com.fiap.biomonitor.domain.model

import java.time.LocalDateTime

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val password: String,
    val city: String,
    val profilePhotoPath: String? = null,
    val points: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
