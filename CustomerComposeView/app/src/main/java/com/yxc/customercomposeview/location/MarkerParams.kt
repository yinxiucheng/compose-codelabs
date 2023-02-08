package com.yxc.customercomposeview.location

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yxc.customercomposeview.R

/**
 * @author yxc
 * @date 2019-12-12
 */
data class MarkerParams(var radius: Dp = 20f.dp,
                        var circleRadius:Dp = 10f.dp,
                        var txtSize: Float = 40f,
                        var kilometer: Int = 0,
                        var markerStr: String = "",
                        var contentWidth: Dp = 50f.dp,
                        var contentHeight: Dp = 58f.dp,
                        var wrapperColor: Int = R.color.location_wrapper,
                        var boardColor: Int = R.color.location_board_color,
                        var circleColor: Int = R.color.location_inner_circle,
                        var drawBottomShader: Boolean = false)