package br.alexandregpereira.hunter.app.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.app.ui.theme.Theme

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Window()
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Window() = Theme {
    Surface {
        Text(text = "Hello World!")
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun DefaultPreview() {
    Window()
}