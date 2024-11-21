package com.example.blisschallenge.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.blisschallenge.data.Avatar
import com.example.blisschallenge.data.Emoji
import com.example.blisschallenge.data.Items
import com.example.blisschallenge.local.avatar.AvatarDao
import com.example.blisschallenge.local.avatar.AvatarEntity
import com.example.blisschallenge.local.emoji.EmojiDao
import com.example.blisschallenge.local.emoji.EmojiEntity
import com.example.blisschallenge.network.GoogleRepoUserSource
import com.example.blisschallenge.network.HttpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    val googleRepos: Flow<PagingData<Items>> = Pager(PagingConfig(pageSize = 1)) {
        GoogleRepoUserSource()
    }.flow.cachedIn(viewModelScope)

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage
    //val toastMessage = _toastMessage.asSharedFlow()

    suspend fun fetchEmojis() {
        _isRefreshing.value = true
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
        _isRefreshing.value = false
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
                _avatars.value = Avatar(avatar.username, avatar.url)
                Log.d("HOME", "fetchAvatar: Avatar fetched from ROOM")

            } else {
                _avatars.value = request.getAvatarByUsername(username)
                avatarDao.insertAvatar(
                    AvatarEntity(
                        username = _avatars.value!!.username.toString(),
                        url = _avatars.value!!.url.toString()
                    )
                )
                Log.d("HOME", "fetchAvatar: Avatar fetched from Request")
            }
        } catch (e: Exception) {
            Log.d("HOME", "ERROR fetchAvatar: ${e.message}")
            _avatars.value = null
        }
    }

    suspend fun fetchAllAvatars() {
        _avatarsList.value = avatarDao.getAllAvatars().map {
            Avatar(it.username, it.url)
        }
        Log.d("HOME", "fetchAllAvatars: Avatars fetched from ROOM")
    }

    fun removeAvatar(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            avatarDao.deleteAvatar(username)

            _avatarsList.value = avatarDao.getAllAvatars().map {
                Avatar(it.username, it.url)
            }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
}