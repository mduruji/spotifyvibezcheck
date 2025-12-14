package com.chatterbox.spotifyvibezcheck.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.data.ChatMessage
import com.chatterbox.spotifyvibezcheck.services.ChatService
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChatRoomViewModel : ViewModel() {

    private val chatService = ChatService()
    private val userService = UserService()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _username = MutableStateFlow("")

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    init {
        _currentUserId.value = FirebaseAuth.getInstance().currentUser?.uid
    }

    fun loadMessages(playlistId: String) {
        viewModelScope.launch {
            val userId = _currentUserId.value
            if (userId != null) {
                _username.value = userService.getUsername(userId) ?: ""
            }
            chatService.getChatMessages(playlistId)
                .catch { e ->
                    Log.e("ChatRoomViewModel", "Error loading messages", e)
                }
                .collect {
                    _messages.value = it
                }
        }
    }

    fun sendMessage(playlistId: String, messageText: String) {
        viewModelScope.launch {
            val userId = _currentUserId.value
            if (userId != null && messageText.isNotEmpty()) {
                val message = ChatMessage(
                    playlistId = playlistId,
                    senderId = userId,
                    senderName = _username.value,
                    message = messageText
                )
                chatService.sendMessage(message)
            }
        }
    }
}
