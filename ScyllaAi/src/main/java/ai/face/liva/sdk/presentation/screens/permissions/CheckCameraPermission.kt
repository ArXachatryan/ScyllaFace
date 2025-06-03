package ai.face.liva.sdk.presentation.screens.permissions

import ai.face.liva.R
import ai.face.liva.sdk.presentation.component.MainButton
import ai.face.liva.sdk.ui.theme.appBackgroundColor
import ai.face.liva.sdk.ui.theme.appLightColor
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import java.util.UUID


@Composable
fun CheckCameraPermission(content: @Composable () -> Unit) {


    var permissionGranted by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }
    var showGoToSettingsBtn by rememberSaveable { mutableStateOf(false) }

    var screenKey by remember { mutableStateOf(UUID.randomUUID().toString()) }
    var checkPermission by remember { mutableStateOf(false) }


    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

            if (checkPermission) {
                screenKey = UUID.randomUUID().toString()
                checkPermission = false
            }

        }
    }


    key(screenKey) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                permissionGranted = true
            } else {
                permissionDenied = true
            }
        }
        LaunchedEffect(Unit) {

            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    when {
        permissionGranted -> content.invoke()
        else -> {

            showGoToSettingsBtn = permissionDenied


            Box(
                Modifier

                    .fillMaxSize()
                    .background(appBackgroundColor),
                contentAlignment = Alignment.TopCenter

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CameraLottie()

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(color = appLightColor, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        text = "Camera permission required", fontSize = 20.sp
                    )

                }

                if (showGoToSettingsBtn) {

                    val context = LocalContext.current

                    MainButton(
                        enabled = true,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        name = "Go to settings",

                        ) {
                        checkPermission = true
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        })


                    }
                }

            }


        }
    }

}

@Composable
fun CameraLottie() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.camera_lottie)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.size(250.dp)

    )

}
