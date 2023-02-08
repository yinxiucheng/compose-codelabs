package com.yxc.customercomposeview.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yxc.customercomposeview.DrawLocationKiloMarker
import com.yxc.customercomposeview.DrawLocationMarker
import com.yxc.customercomposeview.R

@Composable
fun LocationMarkerLayout(){
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .padding(top = 100.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .weight(1f)
                .background(color = Color.Gray)){
                DrawLocationMarker()
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .weight(1f)
                .background(color = Color.Gray)){
                DrawLocationMarker(R.color.location_end_wrapper)

            }
        }
        Row(
            Modifier
                .padding(top = 100.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .weight(1f)
                .background(color = Color.Gray)){
                DrawLocationKiloMarker("3")
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .weight(1f)
                .background(color = Color.Gray)){
                DrawLocationKiloMarker("10")
            }
        }
    }
}