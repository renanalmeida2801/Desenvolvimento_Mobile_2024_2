package com.example.nighteventsapp.ui.screens

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.CaseMap.Title
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.nighteventsapp.models.eventList

@Composable
fun HomeScreen(navController:NavHostController, context: Context){
    createNotificationChanel(context)

    Column{
        val subscribedEvents = eventList.filter { it.isSubscribed.value }

        if(subscribedEvents.isEmpty()){
            Text(
                text = "Eventos inscritos",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subscribedEvents){event ->
                    Card(
                        modifier = Modifier.size(60.dp).clickable { navController.navigate("eventDetails/${event.id}") },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ){
                        Image(
                            painter = painterResource(id = event.imageRes),
                            contentDescription = event.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(eventList){event ->
                Card(
                    modifier = Modifier.padding(vertical = 8.dp).clickable { navController.navigate("eventDetails/${event.id}") },
                    elevation = CardDefaults.cardElevation(4.dp)
                ){
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = event.imageRes),
                                contentDescription = event.title,
                                modifier = Modifier.size(64.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = event.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = event.location,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Icon(
                                imageVector = if(event.isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp).clickable { event.isFavorite.value = !event.isFavorite.value }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            event.isSubscribed.value = !event.isSubscribed.value
                            if(event.isSubscribed.value){
                                sendNotification(context, event.title)
                            }
                        },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if(event.isSubscribed.value) "Inscrito" else "Se inscrever"
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            navController.navigate("eventDetails/${event.id}")
                        },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Ver mais sobre ${event.title}"
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun createNotificationChanel(context: Context){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val name = "Inscrição de eventos"
        val descriptionText = "Canal para notificações de inscrição em eventos"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("EVENT_CHANNEL", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

private fun sendNotification(context: Context, eventTitle: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Verifique se a permissão foi concedida
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Notifique que a permissão é necessária
            Toast.makeText(context, "Permissão para notificações não concedida.", Toast.LENGTH_SHORT).show()
            return
        }
    }

    val builder = NotificationCompat.Builder(context, "EVENT_CHANNEL")
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle("Inscrição confirmada")
        .setContentText("Você foi inscrito no evento $eventTitle")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(eventTitle.hashCode(), builder.build())
    }
}
