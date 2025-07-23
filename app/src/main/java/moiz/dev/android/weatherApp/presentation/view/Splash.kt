package moiz.dev.android.weatherApp.presentation.view

import android.Manifest
import android.graphics.Color
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import moiz.dev.android.weatherApp.R
import moiz.dev.android.weatherApp.Utils.getCurrentLocation
import moiz.dev.android.weatherApp.data.viewModel.WeatherViewModel
import moiz.dev.android.weatherApp.ui.theme.left_grad
import moiz.dev.android.weatherApp.utils.Routes

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Splash(navController: NavController, viewModel: WeatherViewModel) {

    val context = LocalContext.current
    val forecast by viewModel.forecast.observeAsState()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getCurrentLocation(context) { lat, lon ->
                    Log.d("Location", "Latitude: $lat, Longitude: $lon")
                }

                viewModel.loadForcast("Rawalpindi" , 7)
            }
        }
    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    LaunchedEffect(forecast) {
        if(forecast!=null){
            navController.navigate(Routes.ONBOARDING) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        left_grad,
                        androidx.compose.ui.graphics.Color.White,

                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        LaunchedEffect(Unit) {
            delay(5000)
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) {
                    inclusive = true
                }
            }
        }
        Image(painter = painterResource(R.drawable.splash_img), contentDescription = "")
        Text(
            "Weather",
            fontSize = 40.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Forecast",
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal
        )


    }
}

