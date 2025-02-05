package com.example.testandochatmensagens.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun HomeScreen(navController: NavController) {
    val chatId = "12345"
    val userId = "67890"

    Button(onClick = {
        navController.navigate("chat/$chatId/$userId")
    }) {
        Text("Inciar chat!")
    }
}