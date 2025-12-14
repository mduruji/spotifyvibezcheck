package com.chatterbox.spotifyvibezcheck.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ChatMessage(
    val messageId: String = "",
    val playlistId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
