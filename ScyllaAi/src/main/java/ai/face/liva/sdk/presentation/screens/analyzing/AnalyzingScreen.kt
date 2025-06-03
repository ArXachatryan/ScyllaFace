package ai.face.liva.sdk.presentation.screens.analyzing

import ai.face.liva.R
import ai.face.liva.sdk.network.base.ResponseState
import ai.face.liva.sdk.presentation.component.MainButton
import ai.face.liva.sdk.presentation.screens.detection.intent.DetectionIntent
import ai.face.liva.sdk.presentation.screens.detection.screen.cameraFrameSize
import ai.face.liva.sdk.presentation.screens.detection.viewModel.DetectionViewModel
import ai.face.liva.sdk.sdkBuilder.DetectionResultType
import ai.face.liva.sdk.sdkBuilder.ScyllaAiConfiguration
import ai.face.liva.sdk.sdkBuilder.setDetectionResult
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AnalyzingScreen(
    configParams: ScyllaAiConfiguration,
    navController: NavController,
    detectionViewModel: DetectionViewModel = koinViewModel(),
    finishSdk:()->Unit
) {

    val state  by detectionViewModel.state.collectAsState()


    LaunchedEffect(key1 = detectionViewModel, block = {
        detectionViewModel.processIntent(DetectionIntent.CheckLiveness)
    })



    when(state.livenessResult){

        is ResponseState.Error->{

            val coroutineScope = rememberCoroutineScope()

            remember {
                coroutineScope.launch {
                    setDetectionResult(resultType = DetectionResultType.DETECTION_ERROR,"Detection Error")
                }
            }

            if (configParams.showErrorScreen){
                ErrorScreen(navController)
            }else{
                finishSdk.invoke()
            }

        }
        is ResponseState.Loading -> { AnalyzingLottie() }
        is ResponseState.Success -> {
            val coroutineScope = rememberCoroutineScope()
            remember {
                coroutineScope.launch {
                    setDetectionResult(
                        resultType = DetectionResultType.DETECTION_SUCCESS,
                        "Detection Success"
                    )
                }
            }

            if (configParams.showSuccessScreen){
                SuccessScreen(finishSdk)
            }else{
                finishSdk.invoke()
            }
        }
    }


}

@Composable
fun AnalyzingLottie(){

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.detect)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val lottieSize = cameraFrameSize()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(lottieSize)
        )
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SuccessScreen(
    successClick:()->Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        SuccessLottie()
        Text(
            text = "Success!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        MainButton(
            enabled = true,
            modifier = Modifier,
            name = "Continue",

            ) {
            successClick.invoke()
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun SuccessLottie(){

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.success_lottie)
    )
    val progress by animateLottieCompositionAsState(
        composition
    )

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(200.dp)
        )

}



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ErrorScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        ErrorLottie()
        Text(
            text = "Failed!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        MainButton(
            enabled = true,
            modifier = Modifier,
            name = "Try again",

            ) {

            navController.navigateUp()

        }
    }
}

@Composable
fun ErrorLottie(){

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.error_lottie)
    )
    val progress by animateLottieCompositionAsState(
        composition
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.size(200.dp)
    )

}