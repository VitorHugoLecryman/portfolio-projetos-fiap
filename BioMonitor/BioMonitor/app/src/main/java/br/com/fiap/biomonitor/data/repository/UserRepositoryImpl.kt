package br.com.fiap.biomonitor.data.repository

import br.com.fiap.biomonitor.data.local.database.dao.UserDao
import br.com.fiap.biomonitor.data.local.database.entity.UserEntity
import br.com.fiap.biomonitor.domain.model.User
import br.com.fiap.biomonitor.domain.repository.UserRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val userEntity = userDao.login(email, password)
            if (userEntity != null) {
                Result.success(userEntity.toDomain())
            } else {
                Result.failure(Exception("Email ou senha incorretos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(user: User): Result<Long> {
        return try {
            val existingUser = userDao.findByEmail(user.email)
            if (existingUser != null) {
                Result.failure(Exception("Este email já está cadastrado"))
            } else {
                val id = userDao.insert(user.toEntity())
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)?.toDomain()
    }

    override suspend fun findById(id: Long): User? {
        return userDao.findById(id)?.toDomain()
    }

    override suspend fun update(user: User): Result<Unit> {
        return try {
            userDao.update(user.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(user: User): Result<Unit> {
        return try {
            userDao.delete(user.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopUsers(limit: Int): List<User> {
        return userDao.getTopUsers(limit).map { it.toDomain() }
    }

    override suspend fun getTopUsersByCity(city: String, limit: Int): List<User> {
        return userDao.getTopUsersByCity(city, limit).map { it.toDomain() }
    }

    override suspend fun updatePoints(userId: Long, points: Int): Result<Unit> {
        return try {
            userDao.updatePoints(userId, points)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun UserEntity.toDomain(): User {
        return User(
            id = id,
            name = name,
            email = email,
            password = password,
            city = city,
            profilePhotoPath = profilePhotoPath,
            points = points,
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(createdAt),
                ZoneId.systemDefault()
            )
        )
    }


    private fun User.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            name = name,
            email = email,
            password = password,
            city = city,
            profilePhotoPath = profilePhotoPath,
            points = points,
            createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
}
