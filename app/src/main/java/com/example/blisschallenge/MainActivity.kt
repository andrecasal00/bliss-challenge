package com.example.blisschallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.blisschallenge.local.BlissDatabase
import com.example.blisschallenge.ui.theme.BlissChallengeTheme
import com.example.blisschallenge.viewmodels.BlissViewModel
import com.example.blisschallenge.views.AvatarListScreen
import com.example.blisschallenge.views.EmojiListScreen
import com.example.blisschallenge.views.HomeScreen
import com.example.blisschallenge.views.ReposListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlissChallengeTheme {
                val blissViewModel = hiltViewModel<BlissViewModel>()

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "HomeScreen") {
                    composable("HomeScreen") {
                        HomeScreen(navController = navController, viewModel = blissViewModel)
                    }

                    composable("EmojiListScreen") {
                        EmojiListScreen(navController = navController, viewModel = blissViewModel)
                    }

                    composable("AvatarListScreen") {
                        AvatarListScreen(navController = navController, viewModel = blissViewModel)
                    }

                    composable("RepoListScreen") {
                        ReposListScreen(navController = navController, viewModel = blissViewModel)
                    }
                }
            }
        }
    }
}