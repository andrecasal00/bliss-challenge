package com.example.blisschallenge.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.blisschallenge.utilities.NavigationTopBar
import com.example.blisschallenge.viewmodels.BlissViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: BlissViewModel
) {
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = "Bliss Challenge",
                canNavigateBack = false
            )
        },
        containerColor = Color.White,
    ) {
        Surface(
            modifier = Modifier
                .padding(0.dp, it.calculateTopPadding(), 0.dp, it.calculateBottomPadding() + 10.dp)
                .fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                var username by remember { mutableStateOf("") }
                var url by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    url = "https://images.emojiterra.com/google/noto-emoji/unicode-16.0/color/1024px/263a.png"
                }

                val avatar by viewModel.avatars.collectAsState()
                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DisplayAvatarOrEmoji(url = url)

                    Button(
                        onClick = {
                            scope.launch {
                                url = viewModel.generateEmoji()
                                Log.d("HOME", "HomeScreen: $url")
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C3380)),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(0.8f)
                    ) {
                        Text("Random Emoji", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    }

                    Button(
                        onClick = { navController.navigate("EmojiListScreen") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C3380)),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(0.8f)
                    ) {
                        Text("Emoji List", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .fillMaxWidth(0.7f),
                            label = { Text("Search Github Username") },
                        )

                        Button(
                            shape = RoundedCornerShape(12.dp),
                            onClick = {
                                scope.launch {
                                    viewModel.fetchAvatar(username)
                                    if (avatar != null) {
                                        url = avatar!!.url.toString()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C3380)),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { navController.navigate("AvatarListScreen") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C3380)),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.8f)
                    ) {
                        Text("Avatar List", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    }

                    Button(
                        onClick = { navController.navigate("RepoListScreen") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C3380)),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.8f)
                    ) {
                        Text("Google Repos", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
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
                .width(120.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
