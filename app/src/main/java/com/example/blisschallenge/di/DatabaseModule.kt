package com.example.blisschallenge.di

import android.content.Context
import androidx.room.Room
import com.example.blisschallenge.local.BlissDatabase
import com.example.blisschallenge.local.avatar.AvatarDao
import com.example.blisschallenge.local.emoji.EmojiDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BlissDatabase {
        return Room.databaseBuilder(
            context,
            BlissDatabase::class.java,
            "bliss.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAvatarDao(database: BlissDatabase): AvatarDao {
        return database.avatarDao
    }

    @Provides
    fun provideEmojiDao(database: BlissDatabase): EmojiDao {
        return database.emojiDao
    }
}
