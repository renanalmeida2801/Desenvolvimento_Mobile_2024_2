package com.example.testandochatmensagens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testandochatmensagens.repository.ChatRepository
import com.example.testandochatmensagens.ui.screen.ChatScreen
import com.example.testandochatmensagens.ui.screen.HomeScreen
import com.example.testandochatmensagens.ui.theme.TestandoChatMensagensTheme
import com.example.testandochatmensagens.viewmodel.ChatViewModel
import com.example.testandochatmensagens.viewmodel.ChatViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestandoChatMensagensTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home"){
                    composable("home"){
                        HomeScreen(navController)
                    }
                    composable("chat/{chatId}/{userId}",
                        arguments = listOf(
                            navArgument("chatId"){type = NavType.StringType},
                            navArgument("userId"){type = NavType.StringType}
                        )
                    ){ backStackEntry ->
                        val chatId = backStackEntry.arguments?.getString("chatId")?: ""
                        val userId = backStackEntry.arguments?.getString("userId")?:""
                        val repository = ChatRepository()
                        val chatViewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(repository = repository))

                        ChatScreen(viewModel = chatViewModel, userId = userId, chatId = chatId)
                    }
                }
            }
        }
    }
}



