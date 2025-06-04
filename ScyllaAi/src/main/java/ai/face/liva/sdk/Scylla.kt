package ai.face.liva.sdk

import ai.face.liva.sdk.di.networkModule
import ai.face.liva.sdk.di.repositoryModule
import ai.face.liva.sdk.di.useCaseModule
import ai.face.liva.sdk.di.viewModelModule
import ai.face.liva.sdk.presentation.routing.ScreensRouting
import ai.face.liva.sdk.presentation.screens.analyzing.AnalyzingScreen
import ai.face.liva.sdk.presentation.screens.consent.ConsentScreen
import ai.face.liva.sdk.presentation.screens.detection.screen.DetectionScreen
import ai.face.liva.sdk.presentation.screens.uploadDocument.ImageCaptureScreen
import ai.face.liva.sdk.sdkBuilder.ScyllaAiConfiguration
import ai.face.liva.sdk.sdkBuilder.configParams
import ai.face.liva.sdk.sdkBuilder.resultImitation
import ai.face.liva.sdk.ui.theme.ScyllaAITheme
import ai.face.liva.sdk.ui.theme.appBackgroundColor
import ai.face.liva.sdk.ui.theme.appLightColor
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

enum class ScyllaAiDetectionType {

    LIVENESS, FACE_MATCH

}

val topAppBarTitle: MutableState<String> by lazy { mutableStateOf("") }
val showBack: MutableState<Boolean> by lazy { mutableStateOf(false) }

class Scylla : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var configParams:ScyllaAiConfiguration? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("CONFIG", ScyllaAiConfiguration::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("CONFIG") as ScyllaAiConfiguration
        }

        if (configParams == null ){

            configParams=ScyllaAiConfiguration()
        }

        val startDestination = if (configParams.showConsentScreen) ScreensRouting.Consent.route else ScreensRouting.LiveDetection.route

        setContent {
            ScyllaAITheme {

                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = appBackgroundColor,
                                titleContentColor = appLightColor,
                                actionIconContentColor = appLightColor,
                                navigationIconContentColor = appLightColor
                            ),
                            title = {
                                Text(
                                    style = TextStyle(
                                        color = appLightColor,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    text = topAppBarTitle.value, fontSize = 20.sp
                                )


                            },

                            navigationIcon = {
                                if (showBack.value) {
                                    IconButton(modifier = Modifier, onClick = {
                                        val isAtStartDestination =
                                            navController.previousBackStackEntry == null
                                        if (isAtStartDestination) {
                                            finish()
                                        } else {
                                            navController.navigateUp()
                                        }

                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "App Bar Back",
                                            tint = appLightColor,
                                            modifier = Modifier
                                        )
                                    }
                                }

                            }
                        )
                    }) {
                    Box(modifier = Modifier.padding(it)) {

                        AppNavigation(
                            configParams = configParams,
                            activity = this@Scylla,
                            navController = navController,
                            startDestination = startDestination
                        )

                    }

                }

            }
        }

    }

}


@Composable
fun AppNavigation(
    configParams:ScyllaAiConfiguration,
    activity: Activity,
    navController: NavHostController,
    startDestination: String
) {


    NavHost(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 25.dp),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ScreensRouting.Consent.route) {
            showBack(true)
            setAppBarTitle("Consent")
            ConsentScreen(navController = navController, detectionType = configParams.detectionType)
        }

        composable(ScreensRouting.UploadDocument.route) {
            showBack(true)
            setAppBarTitle("Upload file")
            ImageCaptureScreen(navController = navController)
        }
        composable(ScreensRouting.LiveDetection.route) {
            showBack(true)
            setAppBarTitle("Detection")
            DetectionScreen(navController = navController)

        }
        composable(ScreensRouting.AnalyzingScreen.route) {
            showBack(true)
            setAppBarTitle("Analyzing")
            AnalyzingScreen(navController = navController,configParams = configParams) {
                activity.finish()
            }

        }
    }
}


private fun setAppBarTitle(stringResource: String) {
    topAppBarTitle.value = stringResource
}

private fun showBack(show: Boolean = true) {
    showBack.value = show
}
