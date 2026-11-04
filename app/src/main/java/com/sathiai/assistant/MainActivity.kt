package com.sathiai.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sathiai.assistant.companion.Companion
import com.sathiai.assistant.security.SecureStorage
import com.sathiai.assistant.trading.TradingAnalyst
import com.sathiai.assistant.ui.screens.ChatScreen
import com.sathiai.assistant.ui.screens.HomeScreen
import com.sathiai.assistant.ui.screens.SettingsScreen
import com.sathiai.assistant.ui.screens.TradingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SathiAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val secureStorage = SecureStorage(this)
                    val companion = Companion(secureStorage)
                    val tradingAnalyst = TradingAnalyst(secureStorage)

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                onChatClick = { navController.navigate("chat") },
                                onTradingClick = { navController.navigate("trading") },
                                onSettingsClick = { navController.navigate("settings") }
                            )
                        }
                        composable("chat") {
                            ChatScreen(companion = companion, onBack = { navController.popBackStack() })
                        }
                        composable("trading") {
                            TradingScreen(tradingAnalyst = tradingAnalyst, onBack = { navController.popBackStack() })
                        }
                        composable("settings") {
                            SettingsScreen(secureStorage = secureStorage, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
