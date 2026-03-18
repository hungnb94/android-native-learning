package com.tekome.androidnativelearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tekome.androidnativelearning.navigation.Screen
import com.tekome.androidnativelearning.ui.HomeScreen
import com.tekome.androidnativelearning.ui.savedstate.SavedStateDemoScreen
import com.tekome.androidnativelearning.ui.theme.AndroidNativeLearningTheme
import com.tekome.androidnativelearning.ui.viewmodel.ViewModelDemoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidNativeLearningTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(
                            onNavigateToViewModel = {
                                navController.navigate(Screen.ViewModelDemo.route)
                            },
                            onNavigateToSavedState = {
                                navController.navigate(Screen.SavedStateDemo.route)
                            }
                        )
                    }

                    composable(Screen.ViewModelDemo.route) {
                        ViewModelDemoScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.SavedStateDemo.route) {
                        SavedStateDemoScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}