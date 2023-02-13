package com.yxc.customercomposeview.rainbow

import android.graphics.RectF
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.rainbow.RainbowModel.Companion.createTargetModel

@Preview
@Composable
fun Rainbow(){
    drawRainbow()
}

@Composable
fun drawRainbow(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val width = screenWidth * 2 / 3
        Box(modifier = Modifier
            .size(width.dp)
            .align(alignment = Alignment.Center)){
            drawRainbowInner(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun drawRainbowInner(modifier:Modifier) {
    val fractionBg by remember{ mutableStateOf(1f) }

    drawCircle(
        type = RainbowConstant.TARGET_FIRST_TYPE,
        fraction = fractionBg,
        isBg = true,
        modifier
    )
    drawCircle(
        type = RainbowConstant.TARGET_SECOND_TYPE,
        fraction = fractionBg,
        isBg = true,
        modifier
    )
    drawCircle(
        type = RainbowConstant.TARGET_THIRD_TYPE,
        fraction = fractionBg,
        isBg = true,
        modifier
    )

    val animator1 = remember{ Animatable(0f, Float.VectorConverter) }
    val animator2 = remember{ Animatable(0f, Float.VectorConverter) }
    val animator3 = remember{ Animatable(0f, Float.VectorConverter) }

    LaunchedEffect(Unit){
        animator1.animateTo(targetValue = 0.5f, animationSpec = tween(durationMillis = 1000, delayMillis = 1000, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(Unit){
        animator2.animateTo(targetValue = 0.7f, animationSpec = tween(durationMillis = 1000, delayMillis = 1000, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(Unit){
        animator3.animateTo(targetValue = 0.8f, animationSpec = tween(durationMillis = 1000, delayMillis = 1000, easing = FastOutSlowInEasing))
    }

    drawCircle(
        type = RainbowConstant.TARGET_FIRST_TYPE,
        fraction = animator1.value,
        isBg = false,
        modifier
    )
    drawCircle(
        type = RainbowConstant.TARGET_SECOND_TYPE,
        fraction = animator2.value,
        isBg = false,
        modifier
    )
    drawCircle(
        type = RainbowConstant.TARGET_THIRD_TYPE,
        fraction = animator3.value,
        isBg = false,
        modifier
    )
}

@Composable
fun drawCircle(type: Int,
               fraction: Float,
               isBg: Boolean, modifier: Modifier){
    val colorResource = getColorResource(type)
    val color = colorResource(id = colorResource)
    Canvas(modifier = modifier.fillMaxSize()){
        val contentWidth = size.width
        val contentHeight = size.height
        val itemWidth = contentWidth / 7.2f
        val spaceWidth = itemWidth / 6.5f
        val rectF = createTargetRectF(type, itemWidth, spaceWidth, contentWidth, contentHeight)
        val space = if (type == RainbowConstant.TARGET_THIRD_TYPE) spaceWidth/2.0f else spaceWidth
        val sweepAngel = fraction * 180
        val targetModel: RainbowModel = createTargetModel(isBg, type, rectF, itemWidth, space, sweepAngel)
        println("drawRainbow width:${rectF.width()}, height${rectF.height()}")
        if (checkFractionIsSmall(fraction, type)) {
            val roundRectF = createRoundRectF(type, itemWidth, spaceWidth, contentHeight)
            drawRoundRect(
                color = color,
                topLeft = Offset(x = roundRectF.left, y = roundRectF.top),
                size = Size(roundRectF.width(), roundRectF.height()),
                cornerRadius = CornerRadius(spaceWidth / 2.0f, spaceWidth / 2.0f)
            )
        } else {
            withTransform({ translate(left = rectF.left, top = rectF.top) }) {
                targetModel.createComponents()
                targetModel.drawComponents(this, color, isBg)
            }
        }

    }
}

private fun checkFractionIsSmall(fraction: Float, type: Int): Boolean {
    return if (type == RainbowConstant.TARGET_FIRST_TYPE) {
        fraction < RainbowConstant.FIRST_FOUND_ANGLE_FRACTION
    } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
        fraction < RainbowConstant.SECOND_FOUND_ANGLE_FRACTION
    } else {
        fraction < RainbowConstant.THIRD_FOUND_ANGLE_FRACTION
    }
}

private fun createRoundRectF(type: Int, itemWidth:Float, spaceWidth:Float, height:Float): RectF {
    val times: Int = getTimes(type)
    val start: Float = times * (itemWidth + spaceWidth)
    return RectF(start, height / 2 - spaceWidth, start + itemWidth, height / 2)
}

private fun getTimes(type: Int): Int {
    return if (type - 1 > 0) type - 1 else 0
}

fun getColorResource(type: Int): Int {
    if (type == RainbowConstant.TARGET_THIRD_TYPE) {
        return R.color.rainbow_color3
    } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
        return R.color.rainbow_color2
    }
    return R.color.rainbow_color1
}

private fun createTargetRectF(type: Int, itemWidth:Float, spaceWidth:Float, width:Float, height:Float): RectF {
    var times = 0
    if (type == RainbowConstant.TARGET_THIRD_TYPE) {
        times = 2
    } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
        times = 1
    }
    return RectF(
        times * (itemWidth + spaceWidth), times * (itemWidth + spaceWidth),
        width - times * (itemWidth + spaceWidth), height - times * (itemWidth + spaceWidth)
    )
}