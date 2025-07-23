package moiz.dev.android.weatherApp.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import moiz.dev.android.weatherApp.R
import moiz.dev.android.weatherApp.data.model.OnboardingPage
import moiz.dev.android.weatherApp.data.viewModel.WeatherViewModel
import moiz.dev.android.weatherApp.ui.theme.Purple80
import moiz.dev.android.weatherApp.ui.theme.app_grey
import moiz.dev.android.weatherApp.utils.Routes

@Composable
fun OnBoarding(navController: NavController) {
    val onboardingPages = remember {
        listOf(
            OnboardingPage(
                R.drawable.cloudy_night,
                "Detailed Hourly\nForecast",
                "Get in - depth weather\ninformation."
            ),
            OnboardingPage(R.drawable.sunny, "Real-Time\nWeather Map", "Watch the progress of the\nprecipitation to stay informed"),
            OnboardingPage(R.drawable.partly_cloudy, "Weather Around\nthe World", "Add any location you want and\nswipe easily to chnage."),
            OnboardingPage(R.drawable.windy_cloudy, "Detailed Hourly\nForecast", "Get in - depth weather\ninformation.")
        )
    }
    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    val page = onboardingPages[currentPage]
    val progress = when (currentPage) {
        0 -> 0.25f
        1 -> 0.5f
        2 -> 0.75f
        3 -> 1f
        else -> 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Column(modifier = Modifier.weight(0.3f)) {
                Image(
                    painter = painterResource(id = page.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }


            Box(modifier = Modifier.weight(0.4f)) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.onboarding_oval),
                    contentDescription = "null",
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = page.title,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = page.subtitle,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp, color = Color.Gray, textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    CircularProgressButton(
                        progress = progress,
                        onClick = {
                            if (currentPage < 3) currentPage++ else {
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun CircularProgressButton(
    progress: Float,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 6.dp,
            modifier = Modifier
                .fillMaxSize(), color = Purple80
        )

        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(70.dp)
                .background(app_grey, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Next",
                tint = Color.White
            )
        }
    }
}
