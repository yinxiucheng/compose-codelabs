package com.yxc.customercomposeview.waterdrop

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun drawWaterDropBg(){
    val waterDropModelBg by remember {
        mutableStateOf(WaterDropModel.waterDropMBg)
    }
    val color1 = colorResource(id = waterDropModelBg.water1.colorResource)
    val color2 = colorResource(id = waterDropModelBg.water2.colorResource)
    val color3 = colorResource(id = waterDropModelBg.water3.colorResource)
    val color4 = colorResource(id = waterDropModelBg.water4.colorResource)
    val color5 = colorResource(id = waterDropModelBg.water5.colorResource)
    val color6 = colorResource(id = waterDropModelBg.water6.colorResource)
    val color7 = colorResource(id = waterDropModelBg.water7.colorResource)
    val color8 = colorResource(id = waterDropModelBg.water8.colorResource)
    Canvas(modifier = Modifier.fillMaxSize()){
        val contentWidth = size.width
        val contentHeight = size.height
        withTransform({
            translate(left = contentWidth / 2, top = contentHeight / 2)
        }) {
            drawPath(AndroidPath(waterDropModelBg.water8Path), color = color8)
            drawPath(AndroidPath(waterDropModelBg.water7Path), color = color7)
            drawPath(AndroidPath(waterDropModelBg.water6Path), color = color6)
            drawPath(AndroidPath(waterDropModelBg.water5Path), color = color5)
            drawPath(AndroidPath(waterDropModelBg.water4Path), color = color4)
            drawPath(AndroidPath(waterDropModelBg.water3Path), color = color3)
            drawPath(AndroidPath(waterDropModelBg.water2Path), color = color2)
            drawPath(AndroidPath(waterDropModelBg.water1Path), color = color1)
        }
    }
}

@Composable
fun drawWaterDrop(){
    val waterDropModel by remember {
        mutableStateOf(WaterDropModel.waterDropM)
    }
    val color1 = colorResource(id = waterDropModel.water1.colorResource)
    val color2 = colorResource(id = waterDropModel.water2.colorResource)
    val color3 = colorResource(id = waterDropModel.water3.colorResource)
    val color4 = colorResource(id = waterDropModel.water4.colorResource)
    val color5 = colorResource(id = waterDropModel.water5.colorResource)
    val color6 = colorResource(id = waterDropModel.water6.colorResource)
    val color7 = colorResource(id = waterDropModel.water7.colorResource)
    val color8 = colorResource(id = waterDropModel.water8.colorResource)
    Canvas(modifier = Modifier.fillMaxSize()){
        val contentWidth = size.width
        val contentHeight = size.height
        withTransform({
            translate(left = contentWidth / 2, top = contentHeight / 2)
        }) {
            drawPath(AndroidPath(waterDropModel.water8Path), color = color8)
            drawPath(AndroidPath(waterDropModel.water7Path), color = color7)
            drawPath(AndroidPath(waterDropModel.water6Path), color = color6)
            drawPath(AndroidPath(waterDropModel.water5Path), color = color5)
            drawPath(AndroidPath(waterDropModel.water4Path), color = color4)
            drawPath(AndroidPath(waterDropModel.water3Path), color = color3)
            drawPath(AndroidPath(waterDropModel.water2Path), color = color2)
            drawPath(AndroidPath(waterDropModel.water1Path), color = color1)
        }
    }
}

@Preview
@Composable
fun WaterDrop(){
    Box(modifier = Modifier.fillMaxSize()){
        drawWaterDropBg()
        drawWaterDrop()
    }
}