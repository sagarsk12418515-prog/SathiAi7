package com.sathiai.assistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onChatClick: () -> Unit,
    onTradingClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1513))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "স্বাগতম, সাথী!",
            color = Color(0xFF80CBC4),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "আমি আপনার বাংলাভাষী এআই সহকারী। আজ আপনাকে কীভাবে সাহায্য করতে পারি?",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // চ্যাট বাটন
        Button(
            onClick = onChatClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF80CBC4)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "কথা বলুন (Chat)", color = Color(0xFF00332E), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ট্রেডিং বাটন
        Button(
            onClick = onTradingClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A27)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "গোল্ড ট্রেডিং অ্যানালাইসিস", color = Color(0xFF80CBC4), fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // সেটিংস বাটন
        TextButton(onClick = onSettingsClick) {
            Text("সেটিংস (API Key দিন)", color = Color.Gray)
        }
    }
}
