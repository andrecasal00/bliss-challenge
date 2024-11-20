package com.example.blisschallenge.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blisschallenge.data.Avatar
import com.example.blisschallenge.data.Emoji
import com.example.blisschallenge.local.avatar.AvatarDao
import com.example.blisschallenge.local.avatar.AvatarEntity
import com.example.blisschallenge.local.emoji.EmojiDao
import com.example.blisschallenge.local.emoji.EmojiEntity
import com.example.blisschallenge.network.HttpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlissViewModel(
    private val emojiDao: EmojiDao,
    private val avatarDao: AvatarDao
): ViewModel() {
    private val _emojis = MutableStateFlow<List<Emoji>>(emptyList())
    val emojis = _emojis.asStateFlow()

    private val _avatars = MutableStateFlow<Avatar?>(null)
    val avatars = _avatars.asStateFlow()

    private val _avatarsList = MutableStateFlow<List<Avatar>>(emptyList())
    val avatarsList = _avatarsList.asStateFlow()

    private val request = HttpRequest()

    suspend fun fetchEmojis() {
        if (emojiDao.getAllEmojis().isEmpty()) {
            Log.d("HOME", "fetchEmojis: Fetched from REQUEST")
            _emojis.value = request.getAllEmojis()
            emojiDao.insertEmoji(
                _emojis.value.map {
                    EmojiEntity(name = it.name, url = it.url)
                }
            )
        } else {
            Log.d("HOME", "fetchEmojis: Fetched from ROOM")
            _emojis.value = emojiDao.getAllEmojis().map {
                Emoji(it.name, it.url)
            }
        }
    }

    suspend fun generateEmoji(): String {
        fetchEmojis()
        return _emojis.value.random().url
    }

    fun removeEmoji(emoji: Emoji) {
        viewModelScope.launch(Dispatchers.IO) {
            _emojis.value = _emojis.value.filter { it.url != emoji.url }
        }
    }

    suspend fun fetchAvatar(username: String) {
        try {
            val avatar = avatarDao.getAvatarByUsername(username)
            Log.d("HOME", "fetchAvatar: $avatar")

            if (avatar != null) {
                Log.d("HOME", "fetchAvatar: Fetched from ROOM")
                _avatars.value = Avatar(avatar.username, avatar.url)

            } else {
                _avatars.value = request.getAvatarByUsername(username)
                avatarDao.insertAvatar(
                    AvatarEntity(
                        username = _avatars.value!!.username.toString(),
                        url = _avatars.value!!.url.toString()
                    )
                )
            }
        } catch (e: Exception) {
            Log.d("HOME", "ERROR fetchAvatar: ${e.message}")
            _avatars.value = null
        }
    }

    fun fetchAllAvatars() {
        viewModelScope.launch(Dispatchers.IO) {
            _avatarsList.value = avatarDao.getAllAvatars().map {
                Avatar(it.username, it.url)
            }
        }
    }

    fun removeAvatar(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            avatarDao.deleteAvatar(username)

            _avatarsList.value = avatarDao.getAllAvatars().map {
                Avatar(it.username, it.url)
            }
        }
    }
}