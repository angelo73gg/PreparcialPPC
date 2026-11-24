package com.example.preparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.preparcial.presentation.ui.navigation.AppNavGraph
import com.example.preparcial.presentation.ui.theme.PreparcialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as PreparcialApplication).container

        setContent {
            PreparcialTheme {
                AppNavGraph(container = appContainer)
            }
        }
    }
}
