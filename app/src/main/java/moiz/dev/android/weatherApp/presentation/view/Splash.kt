package moiz.dev.android.weatherApp.presentation.view

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import moiz.dev.android.weatherApp.R
import moiz.dev.android.weatherApp.utils.Routes

@Composable
fun Splash(navController: NavController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_splash))
    val progress by animateLottieCompositionAsState(composition)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        androidx.compose.ui.graphics.Color.White,
                        androidx.compose.ui.graphics.Color.Magenta
                    )
                )
            )
            .padding(16.dp)

    ) {
        LottieAnimation(composition, progress, modifier = Modifier.weight(1f))
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH){
                    inclusive = true
                }
            }
        }, 5000)
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    Splash(navController = NavController(LocalContext.current))
}