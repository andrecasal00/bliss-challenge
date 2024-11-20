package com.example.blisschallenge.local.emoji

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmojiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoji(emoji: List<EmojiEntity>)

    @Query("SELECT * FROM tbl_emoji")
    suspend fun getAllEmojis(): List<EmojiEntity>
}