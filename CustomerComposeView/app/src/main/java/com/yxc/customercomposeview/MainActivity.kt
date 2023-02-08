package com.yxc.customercomposeview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yxc.customercomposeview.location.LocationMarkerLayout
import com.yxc.customercomposeview.ui.theme.CustomerComposeViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomerComposeViewTheme {
                LocationMarkerLayout()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CustomerComposeViewTheme {
        LocationMarkerLayout()
    }
}