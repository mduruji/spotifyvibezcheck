package com.chatterbox.spotifyvibezcheck.services

import com.chatterbox.spotifyvibezcheck.data.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatService {

    private val db = FirebaseFirestore.getInstance()

    fun getChatMessages(playlistId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = db.collection("chats")
            .whereEqualTo("playlistId", playlistId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull {
                    it.toObject(ChatMessage::class.java)?.copy(messageId = it.id)
                } ?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    suspend fun sendMessage(message: ChatMessage) {
        db.collection("chats").add(message).await()
    }
}
