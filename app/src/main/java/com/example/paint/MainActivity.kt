package com.example.paint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.paint.ui.theme.PaintTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaintTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DrawingApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DrawingApp(modifier: Modifier = Modifier) {
    var isDrawing by remember { mutableStateOf(false) }
    var isTextInput by remember { mutableStateOf(false) }
    var paintArea: PaintArea? by remember { mutableStateOf(null) }
    var currentText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                isDrawing = true
                isTextInput = false
                paintArea?.isWriting = false
            }) {
                Text(text = "Рисовать")
            }
            Button(onClick = {
                isDrawing = false
                isTextInput = true
                paintArea?.isWriting = true
            }) {
                Text(text = "Писать")
            }
            Button(onClick = {
                paintArea?.clear()
            }) {
                Text(text = "Очистить")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color.White)
        ) {
            if (isDrawing || isTextInput) {
                AndroidView(factory = { context ->
                    PaintArea(context, null).apply {
                        paintArea = this
                    }
                }, modifier = Modifier.fillMaxSize())
            }
        }

        if (isTextInput) {
            TextField(
                value = currentText,
                onValueChange = {
                    currentText = it
                    paintArea?.currentText = currentText
                },
                placeholder = { Text("Введите текст") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}
