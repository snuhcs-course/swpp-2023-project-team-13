package com.team13.fooriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.team13.fooriend.ui.LocationPermissionScreen
import com.team13.fooriend.ui.MapScreen
import com.team13.fooriend.ui.theme.FooriendTheme
import com.team13.fooriend.ui.util.checkForPermission



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // FooriendApp()
            FooriendTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var hasLocationPermission by remember {
                        mutableStateOf(checkForPermission(this))
                    }

                    if (hasLocationPermission) {
                        MapScreen(this)
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


