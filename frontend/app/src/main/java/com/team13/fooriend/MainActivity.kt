package com.team13.fooriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.team13.fooriend.ui.FooriendApp
import com.team13.fooriend.ui.LocationPermissionScreen
import com.team13.fooriend.ui.theme.FooriendTheme
import com.team13.fooriend.ui.util.checkForPermission
import com.team13.fooriend.ui.util.SetStatusBarColor
import androidx.compose.ui.graphics.Color
import com.team13.fooriend.ui.theme.CIvory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FooriendTheme {
                SetStatusBarColor(color = Color.Transparent)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    var hasLocationPermission by remember {
                        mutableStateOf(checkForPermission(this))
                    }

                    if (hasLocationPermission) {
                        FooriendApp(this)
                    } else {
                        LocationPermissionScreen {
                            hasLocationPermission = true
                        }
                    }
                }
            }


        }
    }
}


