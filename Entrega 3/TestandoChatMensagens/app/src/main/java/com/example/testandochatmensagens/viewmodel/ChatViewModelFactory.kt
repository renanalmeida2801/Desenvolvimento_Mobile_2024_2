package com.example.testandochatmensagens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testandochatmensagens.repository.ChatRepository

@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory(private val repository: ChatRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)){
            return ChatViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}