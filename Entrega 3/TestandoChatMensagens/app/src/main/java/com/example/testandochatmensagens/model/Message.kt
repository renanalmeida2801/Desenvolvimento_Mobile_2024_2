package com.example.testandochatmensagens.model

data class Message (
    val senderId: String = "",
    val receiverId: String = "",
    val content: String = "",
    val timeStamp: Long = System.currentTimeMillis()
)