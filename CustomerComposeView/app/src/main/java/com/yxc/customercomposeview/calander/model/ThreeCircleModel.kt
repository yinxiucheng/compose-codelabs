package com.yxc.customercomposeview.calander.model

import android.graphics.Color

class ThreeCircleModel private constructor(
    firstPercentage: Float,
    secondPercentage: Float,
    thirdPercentage: Float
) {
    @JvmField
    var firstPercentage = 0f
    @JvmField
    var secondPercentage = 0f
    @JvmField
    var thirdPercentage = 0f
    @JvmField
    var firstCircleColor = -1
    @JvmField
    var secondCircleColor = -1
    @JvmField
    var thirdCircleColor = -1

    init {
        this.firstPercentage = firstPercentage
        this.secondPercentage = secondPercentage
        this.thirdPercentage = thirdPercentage
    }

    fun setColors(firstCircleColor: Int, secondCircleColor: Int, thirdCircleColor: Int) {
        this.firstCircleColor = firstCircleColor
        this.secondCircleColor = secondCircleColor
        this.thirdCircleColor = thirdCircleColor
    }

    fun setFirstCircleColor(firstCircleColor: Int) {
        this.firstCircleColor = firstCircleColor
    }

    fun setSecondCircleColor(secondCircleColor: Int) {
        this.secondCircleColor = secondCircleColor
    }

    fun setThirdCircleColor(thirdCircleColor: Int) {
        this.thirdCircleColor = thirdCircleColor
    }

    override fun toString(): String {
        return "ThreeCircleModel{" +
                "firstPercentage=" + firstPercentage +
                ", secondPercentage=" + secondPercentage +
                ", thirdPercentage=" + thirdPercentage +
                ", firstCircleColor=" + firstCircleColor +
                ", secondCircleColor=" + secondCircleColor +
                ", thirdCircleColor=" + thirdCircleColor +
                '}'
    }

    companion object {
        fun createDefaultModel(): ThreeCircleModel {
            val model = ThreeCircleModel(0f, 0f, 0f)
            val firstColor = Color.parseColor("#FFB426")
            val secondColor = Color.parseColor("#F95C38")
            val thirdColor = Color.parseColor("#53B9FF")
            model.setColors(firstColor, secondColor, thirdColor)
            return model
        }

        fun createThreeCircleModel(
            firstPercentage: Float,
            secondPercentage: Float,
            thirdPercentage: Float
        ): ThreeCircleModel {
            return ThreeCircleModel(firstPercentage, secondPercentage, thirdPercentage)
        }
    }
}