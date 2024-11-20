package com.example.blisschallenge.network

import android.util.Log
import com.example.blisschallenge.data.Avatar
import com.example.blisschallenge.data.Emoji
import com.example.blisschallenge.data.GoogleRepo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpRequest {
    val httpClient = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getAllEmojis(): List<Emoji> {
        val response: Map<String, String> = httpClient.get("https://api.github.com/emojis").body()
        return response.map {
            Emoji(it.key, it.value)
        }
    }

    suspend fun getAvatarByUsername(username: String): Avatar {
        val response: Avatar = httpClient.get("https://api.github.com/users/${username}").body()
        return Avatar(response.username, response.url)
    }

    suspend fun getGoogleRepos(page: Int = 1): GoogleRepo {
        Log.d("GOOGLE", "GOOGLE REPOS: page: $page")

        val response: GoogleRepo = httpClient.get("https://api.github.com/search/repositories?q=google&per_page=10&page=${page}").body()
        return response

    }
}