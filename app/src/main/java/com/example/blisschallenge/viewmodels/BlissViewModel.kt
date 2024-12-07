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
import com.example.blisschallenge.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlissViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    private val _emojis = MutableStateFlow<List<Emoji>>(emptyList())
    val emojis = _emojis.asStateFlow()

    private val _avatars = MutableStateFlow<Avatar?>(null)
    val avatars = _avatars.asStateFlow()

    private val _avatarsList = MutableStateFlow<List<Avatar>>(emptyList())
    val avatarsList = _avatarsList.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val googleRepos: Flow<PagingData<Items>> = Pager(PagingConfig(pageSize = 1)) {
        GoogleRepoUserSource()
    }.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            fetchEmojis()
        }
    }

    fun fetchEmojis() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val emojis = repository.getAllEmojis()
                _emojis.value = emojis
            } catch (e: Exception) {
                Log.d("HOME", "ERROR fetchEmojis: ${e.message}")
                _emojis.value = emptyList()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun generateEmoji(): String {
        return _emojis.value.random().url
    }

    fun removeEmoji(emoji: Emoji) {
        viewModelScope.launch(Dispatchers.IO) {
            _emojis.value = _emojis.value.filter { it.url != emoji.url }
        }
    }

    fun fetchAvatar(username: String) {
        viewModelScope.launch {
            try {
                val avatar = repository.getAvatar(username)
                _avatars.value = avatar
            } catch (e: Exception) {
                Log.d("HOME", "ERROR fetchAvatar: ${e.message}")
                _avatars.value = null
            }
        }
    }

    suspend fun fetchAllAvatars() {
        val updatedList = repository.getAllAvatars()
        _avatarsList.value = updatedList
        Log.d("HOME", "fetchAllAvatars: Avatars fetched from ROOM")
    }

    fun removeAvatar(username: String) {
        viewModelScope.launch {
            try {
                val updatedList = repository.removeAvatar(username)
                _avatarsList.value = updatedList
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error removing avatar", e)
            }
        }
    }
}