package com.sathiai.assistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sathiai.assistant.trading.TradingAnalyst
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingScreen(
    tradingAnalyst: TradingAnalyst,
    onBack: () -> Unit
) {
    var symbol by remember { mutableStateOf("XAUUSD") }
    var price by remember { mutableStateOf("") }
    var timeframe by remember { mutableStateOf("H1") }
    var analysisResult by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("গোল্ড ট্রেডিং অ্যানালাইসিস", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0E1513))
            )
        },
        containerColor = Color(0xFF0E1513)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Input Fields
            OutlinedTextField(
                value = symbol,
                onValueChange = { symbol = it },
                label = { Text("Symbol (e.g. XAUUSD)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Current Price") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = timeframe,
                onValueChange = { timeframe = it },
                label = { Text("Timeframe (M15, H1, H4)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (price.isNotBlank() && !isLoading) {
                        isLoading = true
                        analysisResult = ""
                        coroutineScope.launch {
                            val result = tradingAnalyst.analyzeMarket(symbol, timeframe, price.toDoubleOrNull() ?: 0.0)
                            analysisResult = result
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF80CBC4))
            ) {
                Text("অ্যানালাইসিস করুন", color = Color(0xFF00332E), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Result Area
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF80CBC4))
            } else if (analysisResult.isNotBlank()) {
                Text(
                    text = analysisResult,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E2A27))
                        .padding(16.dp)
                )
            }
        }
    }
}
