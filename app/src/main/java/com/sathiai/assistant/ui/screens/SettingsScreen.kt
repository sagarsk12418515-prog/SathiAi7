package com.sathiai.assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.sathiai.assistant.security.SecureStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    secureStorage: SecureStorage,
    onBack: () -> Unit
) {
    var apiKey by remember { mutableStateOf(secureStorage.getApiKey("openai") ?: "") }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("সেটিংস", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0E1513))
            )
        },
        containerColor = Color(0xFF0E1513)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OpenAI API Key দিন",
                color = Color(0xFF80CBC4),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = apiKey,
                onValueChange = { 
                    apiKey = it 
                    isSaved = false
                },
                label = { Text("sk-...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    secureStorage.saveApiKey("openai", apiKey)
                    isSaved = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("সেভ করুন")
            }

            if (isSaved) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("সফলভাবে সেভ হয়েছে!", color = Color.Green)
            }
        }
    }
}
