package com.example.blisschallenge.data

import kotlinx.serialization.Serializable

@Serializable
data class Emoji(
    val name: String,
    val url: String,
)
