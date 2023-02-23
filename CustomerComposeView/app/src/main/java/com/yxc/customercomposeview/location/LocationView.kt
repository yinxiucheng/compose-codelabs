package com.yxc.customercomposeview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yxc.customercomposeview.location.LocationMarker
import com.yxc.customercomposeview.location.MarkerParams
import com.yxc.customercomposeview.ui.theme.Purple200
import com.yxc.customercomposeview.ui.theme.Teal200

@Composable
fun DrawRectCircle(componentSize: Dp = 300.dp){
    val canvasSizePx = with(LocalDensity.current) {
        componentSize.toPx()
    }

    val infiniteScale = rememberInfiniteTransition()
    val animatedDotScale by infiniteScale.animateFloat(
        initialValue = 20f,
        targetValue = canvasSizePx / 2,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.size(componentSize)) {
        drawRect(brush = Brush.linearGradient(
            colors = listOf(Purple200, Teal200)
        ), size = size)

        drawCircle(
            color = Color.White,
            center = Offset(x = size.width / 2f, y = size.height / 2f),
            radius = animatedDotScale
        )
    }
}


@Composable
fun DrawLocationMarker(wrapperColor:Int = R.color.location_wrapper){
    val circleRadius by rememberSaveable{ mutableStateOf(25) }
    val radius by rememberSaveable{ mutableStateOf(60) }
    var animatedFloatFraction by remember { mutableStateOf(0f) }
    val radiusDp by animateDpAsState(
        targetValue = (radius * animatedFloatFraction).dp,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 500,
            easing = LinearOutSlowInEasing
        )
    )

    val circleRadiusDp by animateDpAsState(
        targetValue = (circleRadius * animatedFloatFraction).dp,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 500,
            easing = LinearOutSlowInEasing
        )
    )

//    val radiusDp by transition.animateDp(label = "Radius", transitionSpec = ) { fraction ->
//        (radius * fraction).dp
//    }

//    val circleRadiusDp by transition.animateDp(label = "CircleRadius", transitionSpec = {}) { fraction ->
//        (circleRadius * fraction).dp
//    }
//    val infiniteScale = rememberInfiniteTransition()
//    val animatedDotScale by animateFloatAsState(
//        targetValue = 1f,
//        animationSpec = tween(
//            durationMillis = 1000,
//            delayMillis = 100,
//            easing = LinearOutSlowInEasing
//        )
//    )

//    val animatedDotScale by infiniteScale.animateFloat(
//        initialValue = 0.3f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(
//                durationMillis = 1000,
//                easing = FastOutLinearInEasing
//            ),
//            repeatMode = RepeatMode.Reverse
//        )
//    )

//    val radiusDp = (radius * animatedDotScale).dp
//    val circleRadiusDp = (circleRadius * animatedDotScale).dp

    val markerParams by remember {
        derivedStateOf { MarkerParams(radiusDp, circleRadiusDp, wrapperColor = wrapperColor) }
    }

    LaunchedEffect(Unit){
        animatedFloatFraction = 1.0f
    }
    
    val locationMarker = LocationMarker(markerParams)
    val markerViewPath = locationMarker.getPath(markerParams.radius.value)
    val bottomOval = locationMarker.getBottomOval()
    val color = colorResource(id = markerParams.wrapperColor)
    val colorOval = colorResource(R.color.location_bottom_shader)

    Canvas(modifier = Modifier.size(0.dp)){
        drawPath(AndroidPath(markerViewPath), color = color)
        drawPath(AndroidPath(bottomOval), color = colorOval)
    }
}


@Composable
fun DrawLocationKiloMarker(valueStr:String){
    val circleRadius by rememberSaveable{ mutableStateOf(35) }
    val radius by rememberSaveable{ mutableStateOf(60) }

    val markerParams by remember {
        derivedStateOf{ MarkerParams(radius.dp, circleRadius.dp, markerStr = valueStr) }
    }

    val locationMarker = LocationMarker(markerParams)
    val markerViewPath = locationMarker.getPath(markerParams.radius.value)
    val centerCirclePath = locationMarker.getCenterCircle(markerParams.radius.value)
    val color = colorResource(id = markerParams.wrapperColor)
    val innerColor = colorResource(id = markerParams.circleColor)

    val paint = Paint().asFrameworkPaint().apply {
        setColor(-0x1)
        style = android.graphics.Paint.Style.FILL
        strokeWidth = 1f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        textSize = markerParams.txtSize.toFloat()
    }

    val rectF = createTextRectF(locationMarker, paint, markerParams)
    val baseLineY = getTextBaseY(rectF, paint)

    Canvas(modifier = Modifier.size(0.dp)){
        drawPath(AndroidPath(markerViewPath), color = color)
        drawPath(AndroidPath(centerCirclePath), color = innerColor)
        drawIntoCanvas {
            it.nativeCanvas.drawText(markerParams.markerStr,  rectF.left, baseLineY, paint)
        }
    }
}

fun createTextRectF(locationMarker: LocationMarker, paint: NativePaint, markerParams: MarkerParams):RectF{
    val rectF = RectF()
    val width: Float = paint.measureText(markerParams.markerStr)
    val rectFLeft: Float = locationMarker.p3.x - width / 2
    val rectFRight: Float = locationMarker.p3.x + width / 2

    val rectHeight: Float = getTxtHeight(paint)
    val rectTop: Float = locationMarker.p2.y - rectHeight/2 //调整位置，看起来居中
    val rectBottom: Float = locationMarker.p2.y + rectHeight/2
    rectF[rectFLeft, rectTop, rectFRight] = rectBottom
    return rectF
}




