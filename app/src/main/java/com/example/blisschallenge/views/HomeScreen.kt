package com.example.blisschallenge.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.blisschallenge.data.Avatar
import com.example.blisschallenge.viewmodels.BlissViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: BlissViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var username by remember {
            mutableStateOf("")
        }

        var url by remember {
            mutableStateOf("")
        }


        val avatar by viewModel.avatars.collectAsState()

        val scope = rememberCoroutineScope()

       DisplayAvatarOrEmoji(url = url)

        Button(
            onClick = {
                scope.launch {
                    url = viewModel.generateEmoji()
                    Log.d("HOME", "HomeScreen: $url")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Random Emoji")
        }

        Button(
            onClick = {
                navController.navigate("EmojiListScreen")

            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Emoji List")
        }

        TextField(
            value = username,
            onValueChange = {
                username = it
            },
            modifier = Modifier.padding(16.dp),
            label = { Text("Search github username") }
        )

        Button(
            onClick = {
                scope.launch {
                    viewModel.fetchAvatar(username)
                    if (avatar != null) {
                        url = avatar!!.url.toString()
                    }
                }

            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Search Avatar")
        }

        Button(
            onClick = {
                navController.navigate("AvatarListScreen")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Avatar List")
        }

        Button(
            onClick = {
                navController.navigate("RepoListScreen")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Google Repos")
        }
    }
}

@Composable
fun DisplayAvatarOrEmoji(url: String?) {
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillBounds
        )
    }
}