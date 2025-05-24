package org.example.nutrisport

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nutrisport.navigation.SetUpNavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        SetUpNavGraph()
    }
}