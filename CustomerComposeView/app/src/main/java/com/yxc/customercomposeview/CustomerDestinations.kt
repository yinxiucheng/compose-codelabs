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

package com.yxc.customercomposeview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Contract for information needed on every Rally navigation destination
 */
interface RallyDestination {
    val icon: ImageVector
    val route: String
}

/**
 * Rally app navigation destinations
 */
object Overview : RallyDestination {
    override val icon = Icons.Filled.Person
    override val route = "overview"
}

object LocationMarker : RallyDestination {
    override val icon = Icons.Filled.LocationOn
    override val route = "location"
}

object WaterDrop : RallyDestination {
    override val icon = Icons.Filled.List
    override val route = "water_drop"
}

object Rainbow : RallyDestination {
    override val icon = Icons.Filled.Call
    override val route = "rainbow"
}

// Screens to be displayed in the top RallyTabRow
val customerTabRowScreens = listOf(Overview, LocationMarker, WaterDrop, Rainbow)
