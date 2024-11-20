package com.example.blisschallenge.local.avatar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AvatarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvatar(avatar: AvatarEntity)

    @Query("SELECT * FROM tbl_avatar")
    suspend fun getAllAvatars(): List<AvatarEntity>

    @Query("SELECT * FROM tbl_avatar WHERE username = :username")
    suspend fun getAvatarByUsername(username: String): AvatarEntity?

    @Query("DELETE FROM tbl_avatar WHERE username = :username")
    suspend fun deleteAvatar(username: String)
}