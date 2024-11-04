package com.example.lifecicledemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.example.lifecicledemo.ui.theme.LifecicleDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifecicleDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        Log.d("LifecicleDemo", "onCreate chamado")
        Toast.makeText(this, "onCreate chamado", Toast.LENGTH_SHORT).show()
    }
    override fun onStart(){
        super.onStart()

        Log.d("LifecicleDemo", "onStart chamado")
        Toast.makeText(this, "onStart Chamado", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()

        Log.d("LifecicleDemo", "onResume Chamado")
        Toast.makeText(this, "onResume Chamado", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()

        Log.d("LifecicleDemo", "onPause Chamado")
        Toast.makeText(this, "onPause Chamado", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()

        Log.d("LifecicleDemo", "onStop Chamado")
        Toast.makeText(this, "onStop Chamado", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("LifecicleDemo", "onDestroy Chamado")
        Toast.makeText(this, "onDestroy Chamado", Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LifecicleDemoTheme {
        Greeting("Android")
    }
}