package com.example.compose.rally

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.overview.OverviewScreen

@Composable
fun RallyNavHost(navController:NavHostController, modifier:Modifier){
    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) {
        //NaGraphBuilder, 导航图
        composable(route = Overview.route){
            OverviewScreen(
                onClickSeeAllAccounts = {
                    navController.navigateSingleTopTo(Accounts.route)
                },
                onClickSeeAllBills = {
                    navController.navigateSingleTopTo(Bills.route)
                }
            )
        }
        composable(route = Accounts.route) {
            AccountsScreen()
        }
        composable(route = Bills.route) {
            BillsScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
//        popUpTo(startDestination) { saveState = true } - 弹出到导航图的起始目的地，以免在您选择标签页时在返回堆栈上构建大型目的地堆栈
//        在 Rally 中，这意味着，在任何目的地按下返回箭头都会将整个返回堆栈弹出到“Overview”屏幕
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }