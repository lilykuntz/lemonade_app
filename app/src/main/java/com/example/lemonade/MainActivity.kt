package com.example.lemonade

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

var lemonade_title = mutableStateOf("")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MakeLemonade()
                }
            }
        }
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                    Toast.makeText(this, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }
                displayTitle()
            }
    }

    private fun displayTitle() {
        val remoteConfig = Firebase.remoteConfig

        // [START get_config_values]
        lemonade_title.value = remoteConfig[TITLE_KEY].asString()
        // [END get_config_values]
    }
    companion object {
        private const val TAG = "MainActivity"

        // Remote Config keys
        private const val TITLE_KEY = "lemonade_title"
    }
}

var header = mutableStateOf("")
var image = mutableStateOf<Painter?>(null)
var imageDescription = mutableStateOf("")
var lemonSqueezes = 0
var maxSqueezes = (2..4).random()
@Composable
fun MakeLemonade() {
    var lemonadeState by remember { mutableStateOf(1)}
    when(lemonadeState){
        1 -> {
            header.value = stringResource(R.string.tap_tree)
            image.value = painterResource(id = R.drawable.lemon_tree)
            imageDescription.value = stringResource(id = R.string.lemon_tree)

        }
        2 -> {
            header.value = stringResource(R.string.tap_squeeze)
            image.value = painterResource(id = R.drawable.lemon_squeeze)
            imageDescription.value = stringResource(id = R.string.lemon_squeeze)
        }
        3 -> {
            header.value = stringResource(R.string.tap_drink)
            image.value = painterResource(id = R.drawable.lemon_drink)
            imageDescription.value = stringResource(id = R.string.lemon_drink)
        }
        4 -> {
            header.value = stringResource(R.string.tap_restart)
            image.value = painterResource(id = R.drawable.lemon_restart)
            imageDescription.value = stringResource(id = R.string.lemon_restart)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            lemonade_title.value,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(CenterHorizontally),
            fontSize = 24.sp
        )
        Text(
            header.value,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(CenterHorizontally),
            fontSize = 18.sp
        )
        image.value?.let {
            Image(
                painter = it,
                contentDescription = imageDescription.value,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .clickable(
                        enabled = true,
                        onClick = {
                            when (lemonadeState) {
                                1,3 -> lemonadeState++
                                2 -> {
                                    lemonSqueezes++
                                    if(lemonSqueezes >= maxSqueezes) {
                                        lemonadeState++
                                        lemonSqueezes = 0
                                        maxSqueezes = (2..4).random()
                                    }
                                }
                                4 -> lemonadeState = 1
                                else -> lemonadeState = 1
                            }
                        }
                    )
                    .border(
                        BorderStroke(2.dp, Color(105, 205, 216)),
                        shape = RoundedCornerShape(4.dp)
                    )

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LemonadeTheme {
        MakeLemonade()
    }
}