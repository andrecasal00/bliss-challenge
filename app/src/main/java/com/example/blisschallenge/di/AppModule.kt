package com.example.blisschallenge.di

import com.example.blisschallenge.local.avatar.AvatarDao
import com.example.blisschallenge.local.emoji.EmojiDao
import com.example.blisschallenge.network.HttpRequest
import com.example.blisschallenge.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHttpRequest(): HttpRequest = HttpRequest()

    @Provides
    @Singleton
    fun provideRepository(
        httpRequest: HttpRequest,
        avatarDao: AvatarDao,
        emojiDao: EmojiDao
    ): ApiRepository {
        return ApiRepository(httpRequest, avatarDao, emojiDao)
    }
}