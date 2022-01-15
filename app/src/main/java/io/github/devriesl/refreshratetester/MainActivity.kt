package io.github.devriesl.refreshratetester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import io.github.devriesl.refreshratetester.ui.MainPage
import io.github.devriesl.refreshratetester.ui.theme.RefreshRateTesterTheme

class MainActivity : ComponentActivity() {
    private val testerViewModel by lazy { TesterViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RefreshRateTesterTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainPage(testerViewModel)
                }
            }
        }
    }
}
