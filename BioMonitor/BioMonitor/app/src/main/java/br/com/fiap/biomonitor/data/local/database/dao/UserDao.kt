package br.com.fiap.biomonitor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.biomonitor.data.local.database.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun findById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM users ORDER BY points DESC LIMIT :limit")
    suspend fun getTopUsers(limit: Int): List<UserEntity>

    @Query("SELECT * FROM users WHERE city = :city ORDER BY points DESC LIMIT :limit")
    suspend fun getTopUsersByCity(city: String, limit: Int): List<UserEntity>

    @Query("UPDATE users SET points = :points WHERE id = :userId")
    suspend fun updatePoints(userId: Long, points: Int)
}
