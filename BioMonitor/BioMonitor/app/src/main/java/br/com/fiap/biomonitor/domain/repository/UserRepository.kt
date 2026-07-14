package br.com.fiap.biomonitor.domain.repository

import br.com.fiap.biomonitor.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(user: User): Result<Long>
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: Long): User?
    suspend fun update(user: User): Result<Unit>
    suspend fun delete(user: User): Result<Unit>
    suspend fun getTopUsers(limit: Int): List<User>
    suspend fun getTopUsersByCity(city: String, limit: Int): List<User>
    suspend fun updatePoints(userId: Long, points: Int): Result<Unit>
}
