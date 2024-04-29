package online.tripguru.tripguru.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguru.ui.NavGraphs
import online.tripguru.tripguru.ui.theme.JWTAuthKtorAndroidTheme

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