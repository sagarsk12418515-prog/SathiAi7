package com.sathiai.assistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sathiai.assistant.companion.Companion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    companion: Companion,
    onBack: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<Pair<String, Boolean>>()) } // Pair(text, isUser)
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("সাথীর সাথে কথা বলুন", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0E1513))
            )
        },
        containerColor = Color(0xFF0E1513)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            // Message List
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    val (text, isUser) = message
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isUser) Color(0xFF80CBC4) else Color(0xFF1E2A27),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = text,
                                color = if (isUser) Color(0xFF00332E) else Color.White
                            )
                        }
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("মেসেজ লিখুন...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (inputText.isNotBlank() && !isLoading) {
                            val userMsg = inputText
                            messages = messages + Pair(userMsg, true)
                            inputText = ""
                            isLoading = true
                            
                            coroutineScope.launch {
                                val response = companion.chat(userMsg)
                                messages = messages + Pair(response, false)
                                isLoading = false
                            }
                        }
                    }
                ) {
                    Text("পাঠান")
                }
            }
        }
    }
}
