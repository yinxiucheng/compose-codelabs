package com.yxc.customercomposeview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yxc.customercomposeview.location.LocationMarkerLayout
import com.yxc.customercomposeview.overviewscreen.OverviewScreen
import com.yxc.customercomposeview.rainbow.Rainbow
import com.yxc.customercomposeview.waterdrop.WaterDrop

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
                onClickLocationMarker = {
                    navController.navigateSingleTopTo(LocationMarker.route)
                },
                onClickWaterDrop = {
                    navController.navigateSingleTopTo(WaterDrop.route)
                },
                onClickRainbow = {
                    navController.navigateSingleTopTo(Rainbow.route)
                }
            )
        }
        composable(route = LocationMarker.route) {
            LocationMarkerLayout()
        }
        composable(route = WaterDrop.route) {
            WaterDrop()
        }
        composable(route = Rainbow.route,) {
            Rainbow()
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
