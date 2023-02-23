package com.yxc.customercomposeview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yxc.customercomposeview.component.RallyTabRow
import com.yxc.customercomposeview.ui.theme.CustomerComposeViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomerComposeViewApp()
        }
    }
}

@Composable
fun CustomerComposeViewApp() {
    CustomerComposeViewTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = customerTabRowScreens.find { it.route == currentDestination?.route }?:Overview

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = customerTabRowScreens,
                    onTabSelected = { newScreen -> navController.navigateSingleTopTo(newScreen.route) },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            //NavHost Container.
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CustomerComposeViewApp()
}