package com.example.testandochatmensagens.repository

import android.util.Log
import com.example.testandochatmensagens.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()

    fun sendMessage(chatId: String, message: Message){
        db.collection("chats").document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener { Log.d("Chat", "Mensagem enviada!") }
            .addOnFailureListener { Log.e("Chat", "Erro ao enviar mensagem", it) }
    }

    fun listenForMessage(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = db.collection("chats").document(chatId)
            .collection("messages").orderBy("timeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener{snapshot, e ->
                if (e != null){
                    Log.e("FireStore", "Erro ao buscar a mensagem", e)
                    return@addSnapshotListener
                }
                val messages = snapshot?.toObjects(Message::class.java)?: emptyList()
                trySend(messages).isSuccess
            }
        awaitClose{listener.remove()}
    }

}