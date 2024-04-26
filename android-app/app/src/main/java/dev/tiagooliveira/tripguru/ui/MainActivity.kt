package dev.tiagooliveira.tripguru.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.tiagooliveira.tripguru.ui.theme.JWTAuthKtorAndroidTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JWTAuthKtorAndroidTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}