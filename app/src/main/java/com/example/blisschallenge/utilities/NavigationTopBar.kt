package com.example.blisschallenge.utilities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopBar(
    modifier: Modifier = Modifier,
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    if (canNavigateBack) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold)
            },
            actions = { actions() },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "arrow back"
                    )
                }
            },
            modifier = modifier,
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    } else {
        CenterAlignedTopAppBar(
            title = {
                Text(text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold)
            },
            actions = { actions() },
            modifier = modifier,
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    }
}