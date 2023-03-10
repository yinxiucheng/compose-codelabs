package com.yxc.basicstatecodelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaterCounter(modifier: Modifier = Modifier){
    Column(modifier = modifier.padding(16.dp)) {
        var count = 0
        Text(text = "You've had $count glasses.", modifier = modifier.padding(6.dp))
        Button(onClick = { count ++ }, modifier = modifier.padding(top=8.dp)) {
            Text(text = "add one")
        }
    }
}