package com.yxc.customercomposeview.waterdrop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun WaterDrop(){
    Box(modifier = Modifier.fillMaxSize()){
        drawWaterDropBg()
        drawWaterDrop()
        for (num in 1 .. 7){
            drawWaterDropScan(delayTime = num * 2000)
        }
    }
}