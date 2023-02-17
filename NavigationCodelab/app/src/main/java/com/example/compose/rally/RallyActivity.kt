/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        var currentScreen: RallyDestination by remember { mutableStateOf(Overview) }
        val navController = rememberNavController()
        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = rallyTabRowScreens,
                    onTabSelected = { newScreen -> navController.navigateSingleTopTo(newScreen.route) },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            //NavHost Container.
            NavHost(
                navController = navController,
                startDestination = Overview.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                //NaGraphBuilder, 导航图
                composable(route = Overview.route){
                    Overview.screen()
                }
                composable(route = Accounts.route) {
                    Accounts.screen()
                }
                composable(route = Bills.route) {
                    Bills.screen()
                }
                // builder parameter will be defined here as the graph
            }
//            Box(Modifier.padding(innerPadding)) {
//                currentScreen.screen()
//            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
//        popUpTo(startDestination) { saveState = true } - 弹出到导航图的起始目的地，以免在您选择标签页时在返回堆栈上构建大型目的地堆栈
//        在 Rally 中，这意味着，在任何目的地按下返回箭头都会将整个返回堆栈弹出到“Overview”屏幕
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
