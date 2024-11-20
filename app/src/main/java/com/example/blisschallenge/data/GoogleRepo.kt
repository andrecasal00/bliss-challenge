package com.example.blisschallenge.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GoogleRepo(
    var items: List<Items>,
)

@Serializable
data class Items(
    @SerialName("full_name")
    val url: String
)
