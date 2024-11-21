package com.example.blisschallenge.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.blisschallenge.utilities.NavigationTopBar
import com.example.blisschallenge.viewmodels.BlissViewModel

@Composable
fun EmojiListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: BlissViewModel
) {
    var goBackCounter by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            NavigationTopBar(
                title = "List of Emojis",
                canNavigateBack = true,
                navigateUp = {
                    if (goBackCounter==0) {
                        navController.popBackStack()
                        goBackCounter++
                    }
                }
            )
        },
    ) { innerPadding ->

        LaunchedEffect(Unit) {
            viewModel.fetchEmojis()
        }

        val emojis by viewModel.emojis.collectAsState()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (emojis.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    items(emojis) { emoji ->
                        AsyncImage(
                            model = emoji.url,
                            contentDescription = emoji.name,
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .padding(4.dp)
                                .clickable(onClick = {
                                    Log.d("TAG", "EmojiListScreen: CLICADO")
                                    viewModel.removeEmoji(emoji)
                                })
                        )
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(innerPadding),
                )
            }
        }
    }
}
