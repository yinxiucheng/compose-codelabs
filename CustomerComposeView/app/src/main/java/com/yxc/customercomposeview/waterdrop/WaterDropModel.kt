package com.yxc.customercomposeview.waterdrop

import android.graphics.Path
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.common.dip2pxF

class WaterDropModel {
    var water1: ComposeBezierCircle
    var water2: ComposeBezierCircle
    var water3: ComposeBezierCircle
    var water4: ComposeBezierCircle
    var water5: ComposeBezierCircle
    var water6: ComposeBezierCircle
    var water7: ComposeBezierCircle
    var water8: ComposeBezierCircle

    var water1Path: Path
    var water2Path: Path
    var water3Path: Path
    var water4Path: Path
    var water5Path: Path
    var water6Path: Path
    var water7Path: Path
    var water8Path: Path

    constructor(radius: Float, colorArray: Array<Int>) {
        water1 = ComposeBezierCircle(radius, 1, colorArray[0])
        water2 = ComposeBezierCircle(radius, 2, colorArray[1])
        water3 = ComposeBezierCircle(radius, 3, colorArray[2])
        water4 = ComposeBezierCircle(radius, 4, colorArray[3])
        water5 = ComposeBezierCircle(radius, 5, colorArray[4])
        water6 = ComposeBezierCircle(radius, 6, colorArray[5])
        water7 = ComposeBezierCircle(radius, 7, colorArray[6])
        water8 = ComposeBezierCircle(radius, 8, colorArray[7])
        water1Path = water1.getPath()
        water2Path = water2.getPath()
        water3Path = water3.getPath()
        water4Path = water4.getPath()
        water5Path = water5.getPath()
        water6Path = water6.getPath()
        water7Path = water7.getPath()
        water8Path = water8.getPath()
    }

    fun drawScan(drawScope: DrawScope, colorArray: Array<Color>, alphaArray: Array<Animatable<Float, AnimationVector1D>>){
        drawScope.apply {
            drawPath(AndroidPath(water8Path), color = colorArray[7], alpha = alphaArray[6].value)
            drawPath(AndroidPath(water7Path), color = colorArray[6], alpha = alphaArray[5].value)
            drawPath(AndroidPath(water6Path), color = colorArray[5], alpha = alphaArray[4].value)
            drawPath(AndroidPath(water5Path), color = colorArray[4], alpha = alphaArray[3].value)
            drawPath(AndroidPath(water4Path), color = colorArray[3], alpha = alphaArray[2].value)
            drawPath(AndroidPath(water3Path), color = colorArray[2], alpha = alphaArray[1].value)
            drawPath(AndroidPath(water2Path), color = colorArray[1], alpha = alphaArray[0].value)
            drawPath(AndroidPath(water1Path), color = colorArray[0])
        }
    }

    fun drawWaterDrop(drawScope: DrawScope, colorArray: Array<Color>, alphaArray: Array<Animatable<Float, AnimationVector1D>>){
        drawScope.apply {
            drawPath(AndroidPath(water8Path), color = colorArray[7], alpha = alphaArray[7].value)
            drawPath(AndroidPath(water7Path), color = colorArray[6], alpha = alphaArray[6].value)
            drawPath(AndroidPath(water6Path), color = colorArray[5], alpha = alphaArray[5].value)
            drawPath(AndroidPath(water5Path), color = colorArray[4], alpha = alphaArray[4].value)
            drawPath(AndroidPath(water4Path), color = colorArray[3], alpha = alphaArray[3].value)
            drawPath(AndroidPath(water3Path), color = colorArray[2], alpha = alphaArray[2].value)
            drawPath(AndroidPath(water2Path), color = colorArray[1], alpha = alphaArray[1].value)
            drawPath(AndroidPath(water1Path), color = colorArray[0], alpha = alphaArray[0].value)
        }
    }

    companion object {
        private val radius = dip2pxF(24f)
        val waterDropMBg = WaterDropModel(
            radius, arrayOf(
                R.color.water_n_drop1,
                R.color.water_n_drop2,
                R.color.water_n_drop3,
                R.color.water_n_drop4,
                R.color.water_n_drop5,
                R.color.water_n_drop6,
                R.color.water_n_drop7,
                R.color.water_n_drop8,
            )
        )

        val waterDropM = WaterDropModel(
            radius, arrayOf(
                R.color.water_drop1,
                R.color.water_drop2,
                R.color.water_drop3,
                R.color.water_drop4,
                R.color.water_drop5,
                R.color.water_drop6,
                R.color.water_drop7,
                R.color.water_drop8,
            )
        )

        val waterDropMScan = WaterDropModel(
            radius, arrayOf(
                R.color.water_drop1,
                R.color.water_drop2,
                R.color.water_drop3,
                R.color.water_drop4,
                R.color.water_drop5,
                R.color.water_drop6,
                R.color.water_drop7,
                R.color.water_drop8,
            )
        )
    }


}