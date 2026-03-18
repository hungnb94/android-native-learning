package com.tekome.androidnativelearning.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ViewModelDemo : Screen("viewmodel_demo")
    object SavedStateDemo : Screen("saved_state_demo")
}
