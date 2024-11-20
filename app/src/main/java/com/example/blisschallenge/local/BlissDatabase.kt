package com.example.blisschallenge.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.blisschallenge.local.avatar.AvatarDao
import com.example.blisschallenge.local.avatar.AvatarEntity
import com.example.blisschallenge.local.emoji.EmojiDao
import com.example.blisschallenge.local.emoji.EmojiEntity

@Database(
    entities = [AvatarEntity::class, EmojiEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class BlissDatabase: RoomDatabase() {
    abstract val emojiDao: EmojiDao
    abstract val avatarDao: AvatarDao
}