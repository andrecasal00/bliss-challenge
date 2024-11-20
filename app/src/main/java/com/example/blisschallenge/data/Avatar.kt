package com.example.blisschallenge.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Avatar(
    @SerialName("login")
    val username: String?,
    @SerialName("avatar_url")
    val url: String?,
)
