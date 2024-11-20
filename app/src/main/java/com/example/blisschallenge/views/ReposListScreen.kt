package com.example.blisschallenge.views

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.blisschallenge.data.Items
import com.example.blisschallenge.local.avatar.AvatarEntity
import com.example.blisschallenge.viewmodels.BlissViewModel

@Composable
fun ReposListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: BlissViewModel
) {
    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->

        val repos: LazyPagingItems<Items> = viewModel.googleRepos.collectAsLazyPagingItems()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Google Repo's List",
                    fontSize = 25.sp
                )
            }

            LazyColumn {
                items(
                    count = repos.itemCount,
                    key = repos.itemKey { repo -> repo.url },
                    contentType = repos.itemContentType { repo -> "repo" }
                ) { index ->
                    val repo = repos[index]
                    if (repo != null) {
                        Text(
                            text = repo.url,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

