package com.example.blisschallenge.repository

import com.example.blisschallenge.data.Avatar
import com.example.blisschallenge.data.Emoji
import com.example.blisschallenge.local.avatar.AvatarDao
import com.example.blisschallenge.local.avatar.AvatarEntity
import com.example.blisschallenge.local.emoji.EmojiDao
import com.example.blisschallenge.local.emoji.EmojiEntity
import com.example.blisschallenge.network.HttpRequest

class ApiRepository(
    private val httpRequest: HttpRequest,
    private val avatarDao: AvatarDao,
    private val emojiDao: EmojiDao
) {

    suspend fun getAvatar(username: String): Avatar {
        val cachedAvatar = avatarDao.getAvatarByUsername(username)
        return if (cachedAvatar != null) {
            Avatar(cachedAvatar.username, cachedAvatar.url)
        } else {
            // Fetch from the network and cache it
            val avatar = httpRequest.getAvatarByUsername(username)
            avatarDao.insertAvatar(
                AvatarEntity(
                    username = avatar.username.toString(),
                    url = avatar.url.toString()
                )
            )
            return avatar
        }
    }

    suspend fun getAllAvatars(): List<Avatar> {
        return avatarDao.getAllAvatars().map {
            Avatar(it.username, it.url)
        }
    }

    suspend fun removeAvatar(username: String): List<Avatar> {
        avatarDao.deleteAvatar(username)

        return getAllAvatars()
    }

    suspend fun getAllEmojis(): List<Emoji> {
        val cachedEmojis = emojiDao.getAllEmojis()
        return if (cachedEmojis.isNotEmpty()) {
            // Return from the database
            cachedEmojis.map {
                Emoji(it.name, it.url)
            }
        } else {
            // Fetch from the network and cache it
            val emojis = httpRequest.getAllEmojis()
            emojiDao.insertEmoji(
                emojis.map {
                    EmojiEntity(name = it.name, url = it.url)
                }
            )
            return emojis
        }
    }
}
