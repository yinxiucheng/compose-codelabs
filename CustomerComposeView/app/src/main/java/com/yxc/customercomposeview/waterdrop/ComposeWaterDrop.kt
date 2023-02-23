package com.yxc.customercomposeview.waterdrop

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch


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

private fun myKeyframs(num:Int):KeyframesSpec<Float>{
    return keyframes{
        durationMillis = 3000
        delayMillis = num * 2000
        0.5f at 1000 with LinearEasing
        0.2f at 2000 with LinearEasing
    }
}

@Composable
fun drawWaterDrop(){
    val waterDropModel by remember {
        mutableStateOf(WaterDropModel.waterDropM)
    }
    val animAlpha1 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha2 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha3 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha4 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha5 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha6 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha7 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha8 = remember { Animatable(0f, Float.VectorConverter) }
    
    LaunchedEffect(Unit){
        launch { animAlpha1.animateTo(1f, animationSpec = myKeyframs(0)) }
        launch { animAlpha2.animateTo(1f, animationSpec = myKeyframs(1)) }
        launch { animAlpha3.animateTo(1f, animationSpec = myKeyframs(2)) }
        launch { animAlpha4.animateTo(1f, animationSpec = myKeyframs(3)) }
        launch { animAlpha5.animateTo(1f, animationSpec = myKeyframs(4)) }
        launch { animAlpha6.animateTo(1f, animationSpec = myKeyframs(5)) }
        launch { animAlpha7.animateTo(1f, animationSpec = myKeyframs(6)) }
        launch { animAlpha8.animateTo(1f, animationSpec = myKeyframs(7)) }
    }

    val colorArray:Array<Color> = arrayOf(
        colorResource(id = waterDropModel.water1.colorResource),
        colorResource(id = waterDropModel.water2.colorResource),
        colorResource(id = waterDropModel.water3.colorResource),
        colorResource(id = waterDropModel.water4.colorResource),
        colorResource(id = waterDropModel.water5.colorResource),
        colorResource(id = waterDropModel.water6.colorResource),
        colorResource(id = waterDropModel.water7.colorResource),
        colorResource(id = waterDropModel.water8.colorResource))

    val alphaArray: Array<Animatable<Float, AnimationVector1D>> = arrayOf(animAlpha1, animAlpha2,
        animAlpha3, animAlpha4, animAlpha5, animAlpha6, animAlpha7, animAlpha8)
    
    Canvas(modifier = Modifier.fillMaxSize()){
        val contentWidth = size.width
        val contentHeight = size.height
        withTransform({
            translate(left = contentWidth / 2, top = contentHeight / 2)
        }) {
            waterDropModel.drawWaterDrop(this, colorArray, alphaArray)
        }
    }
}

private fun myKeyframs2(durationMillisParams:Int, delayMillisParams:Int, frames:Map<Float, Int>):KeyframesSpec<Float>{
    return keyframes{
        durationMillis = durationMillisParams
        delayMillis = delayMillisParams
        for ((valueF, timestamp) in frames){
           valueF at timestamp
        }
    }
}

@Composable
fun drawWaterDropScan(delayTime:Int){
    val waterDropModel by remember {
        mutableStateOf(WaterDropModel.waterDropMScan)
    }
    val delayCurrent by rememberUpdatedState(newValue = delayTime)
    val animAlpha2 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha3 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha4 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha5 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha6 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha7 = remember { Animatable(0f, Float.VectorConverter) }
    val animAlpha8 = remember { Animatable(0f, Float.VectorConverter) }

    LaunchedEffect(Unit){
        launch {
            animAlpha2.animateTo(0f, animationSpec = myKeyframs2(700,  0 + delayCurrent,
                mutableMapOf<Float, Int>().apply { put(1f, 350) }))
        }
        launch{
            animAlpha3.animateTo(0f, animationSpec = myKeyframs2(630, 233 + delayCurrent,
                mutableMapOf<Float, Int>().apply { put(0.8f, 315) }))
        }
        launch{
            animAlpha4.animateTo(0f, animationSpec = myKeyframs2(630, 383 + delayCurrent,
                mutableMapOf<Float, Int>().apply { put(0.55f, 315) }))
        }
        launch {
            animAlpha5.animateTo(0f, animationSpec = myKeyframs2(650, 533 + delayCurrent,
                mutableMapOf<Float, Int>().apply { put(0.5f, 325) }))
        }
        launch {
            animAlpha6.animateTo(0f, animationSpec = myKeyframs2(650, 667 + delayCurrent,
                mutableMapOf<Float, Int>().apply { put(0.45f, 325) }))
        }
        launch {
            animAlpha7.animateTo(0f, animationSpec = myKeyframs2(567, 816 + delayCurrent,
                mutableMapOf<Float, Int>().apply { put(0.35f, 283) }))
        }
        launch {
            animAlpha8.animateTo(0f, animationSpec = myKeyframs2(433, 983 + delayCurrent,
                mutableMapOf<Float, Int>().apply { put(0.3f, 216) }))
        }
    }


    val colorArray:Array<Color> = arrayOf(
        colorResource(id = waterDropModel.water1.colorResource),
        colorResource(id = waterDropModel.water2.colorResource),
        colorResource(id = waterDropModel.water3.colorResource),
        colorResource(id = waterDropModel.water4.colorResource),
        colorResource(id = waterDropModel.water5.colorResource),
        colorResource(id = waterDropModel.water6.colorResource),
        colorResource(id = waterDropModel.water7.colorResource),
        colorResource(id = waterDropModel.water8.colorResource))

    val alphaArray: Array<Animatable<Float, AnimationVector1D>> = arrayOf(animAlpha2,
        animAlpha3, animAlpha4, animAlpha5, animAlpha6, animAlpha7, animAlpha8)

    Canvas(modifier = Modifier.fillMaxSize()){
        val contentWidth = size.width
        val contentHeight = size.height
        withTransform({
            translate(left = contentWidth / 2, top = contentHeight / 2)
        }) {
            waterDropModel.drawScan(this, colorArray, alphaArray)
        }
    }
}

