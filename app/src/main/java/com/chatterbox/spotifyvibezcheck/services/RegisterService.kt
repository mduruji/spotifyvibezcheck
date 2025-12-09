package com.chatterbox.spotifyvibezcheck.services

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RegisterService(private val authService: AuthService) {

    private val db = FirebaseFirestore.getInstance()

    suspend fun register(username: String, email: String, password: String): FirebaseUser? {
        val user = authService.signup(email, password)
        if (user != null) {
            val userMap = hashMapOf(
                "userId" to user.uid,
                "username" to username,
                "photoUrl" to "",
                "friends" to emptyList<String>(),
                "createdAt" to FieldValue.serverTimestamp()
            )
            db.collection("users").document(user.uid).set(userMap).await()
        }
        return user
    }
}
