package com.example.testandochatmensagens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testandochatmensagens.model.Message
import com.example.testandochatmensagens.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository): ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun sendMessage(chatId: String, text: String, senderId: String, receiverId: String ){
        val newMessage = Message(senderId, receiverId, text, System.currentTimeMillis())
        chatRepository.sendMessage(chatId,newMessage)
    }

    fun listenForMessages(chatId: String){
        viewModelScope.launch {
            chatRepository.listenForMessage(chatId).collect{newMessages ->
                _messages.value = newMessages
            }
        }
    }
}