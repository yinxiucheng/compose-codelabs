/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yxc.customercomposeview.overviewscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun OverviewScreen(
    onClickLocationMarker: () -> Unit = {},
    onClickWaterDrop: () -> Unit = {},
    onClickRainbow: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    ) {

        Row(modifier = Modifier) {
            Button(onClick = { onClickLocationMarker() }, modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp)) {
                Text(text = "LocationMarker")
            }

            Button(onClick = { onClickWaterDrop() }, modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp)) {
                Text(text = "WaterDrop")
            }
        }

        Row(modifier = Modifier) {
            Button(onClick = { onClickRainbow() }, modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp)) {
                Text(text = "Rainbow")
            }

            Button(onClick = {  }, modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp)) {
                Text(text = "Calendar")
            }
        }
    }
}