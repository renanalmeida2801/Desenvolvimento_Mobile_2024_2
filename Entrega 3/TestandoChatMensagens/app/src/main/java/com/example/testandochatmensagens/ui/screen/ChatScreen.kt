package com.example.testandochatmensagens.ui.screen

import android.media.midi.MidiSender
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testandochatmensagens.model.Message
import com.example.testandochatmensagens.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel, chatId: String, userId: String) {
    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(chatId) {
        viewModel.listenForMessages(chatId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                MessageBuble(message, isCurrentUser = message.senderId == userId)
            }
        }

        MessageInput(onSend = { text ->
            viewModel.sendMessage(chatId, text, senderId = userId, receiverId = "outroUsuario")
        })
    }
}

@Composable
fun MessageBuble(message: Message, isCurrentUser: Boolean){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if(isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    ){
        Text(
            text =message.content,
            modifier = Modifier
                .padding(8.dp)
                .background(if(isCurrentUser) Color.Blue else Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            color = Color.White
        )
    }
}

@Composable
fun MessageInput(onSend: (String) ->Unit){
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        TextField(
            value = text,
            onValueChange = {text = it},
            modifier = Modifier.weight(1f),
            placeholder = { Text("Digite sua mensagem...") }
        )
        Button(onClick = {
            if(text.isNotEmpty()){
                onSend(text)
                text = ""
            }
        }) {
            Text("enviar")
        }
    }
}