package ai.face.liva.sdk.presentation.screens.detection.screen

import ai.face.liva.sdk.app.liveness.FaceLivenessAnalyzer
import ai.face.liva.sdk.presentation.routing.ScreensRouting
import ai.face.liva.sdk.presentation.screens.permissions.CheckCameraPermission
import ai.face.liva.sdk.ui.theme.appLightColor
import ai.face.liva.sdk.ui.theme.errorColor
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlin.math.min


@SuppressLint("UnrememberedMutableState")
@Composable
fun DetectionScreen(
    navController: NavController,

    ) {

    var instructionMessage by remember { mutableStateOf("") }

    val handleDetected: (ArrayList<Bitmap>) -> Unit = {
        navController.navigate(ScreensRouting.AnalyzingScreen.route)
    }

    val handleError: (String) -> Unit = {

    }
    val handleInstruction: (String) -> Unit = {
        if (instructionMessage != it) {
            instructionMessage = it
        }

    }

    CheckCameraPermission {
        Box(modifier = Modifier.fillMaxSize()) {

            FaceLivenessDetectionScreen(
                onLivenessDetected = handleDetected,
                onError = handleError,
                instructionMessage = handleInstruction

            )

            InstructionsOverlay(instructionMessage)
        }
    }

}

@Composable
fun FaceLivenessDetectionScreen(
    onLivenessDetected: (ArrayList<Bitmap>) -> Unit,
    onError: (String) -> Unit,
    instructionMessage: (String) -> Unit
) {
    val cameraOvalSize = cameraFrameSize()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val onLivenessDetectedState by rememberUpdatedState(onLivenessDetected)
    val onErrorState by rememberUpdatedState(onError)
    val instruction by rememberUpdatedState(instructionMessage)

    val faceAnalyzer = remember(cameraOvalSize) {
        FaceLivenessAnalyzer(
            cameraOvalSize = cameraOvalSize,
            onLivenessDetected = { bitmap -> onLivenessDetectedState(bitmap) },
            onError = { message -> onErrorState(message) },
            instructionMessage = { message -> instruction(message) }
        )
    }

    val previewViewRef = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().apply {
                    surfaceProvider = previewViewRef.surfaceProvider
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply {
                        setAnalyzer(ContextCompat.getMainExecutor(context), faceAnalyzer)
                    }

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                onErrorState("Camera binding failed: ${e.localizedMessage}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(cameraOvalSize)
                .clip(CircleShape)
                .background(Color.Black)
        ) {
            AndroidViewBindingPreviewView(previewView = previewViewRef)
        }

        CircularMaskOverlay(sizeDp = cameraOvalSize)
    }
}

@Composable
fun AndroidViewBindingPreviewView(previewView: PreviewView) {
    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun CircularMaskOverlay(sizeDp: Dp) {

    Canvas(modifier = Modifier.fillMaxSize()) {

        val canvasWidth = size.width
        val canvasHeight = size.height
        val circleRadius = sizeDp.toPx() / 2f
        val center = Offset(canvasWidth / 2, canvasHeight / 2)

        val gradientStroke = Brush.sweepGradient(
            0f to Color.Cyan,
            0.25f to Color.Blue,
            0.5f to Color.Magenta,
            0.75f to Color.Red,
            1f to Color.Cyan
        )

        drawRect(color = Color.Transparent)

        drawCircle(
            color = Color.Transparent,
            radius = circleRadius,
            center = center,
        )

        drawCircle(
            brush = gradientStroke,
            radius = circleRadius,
            center = center,
            style = Stroke(width = 10.dp.toPx())
        )


    }
}

@Composable
fun cameraFrameSize(): Dp {

    LocalConfiguration.current.also {

        return (min(it.screenWidthDp, it.screenHeightDp) * 0.8).dp
    }

}


@Composable
fun InstructionsOverlay(message: String) {

    Log.e("bdjbdjbv", message)
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(bottom = 15.dp),
            style = TextStyle(color = appLightColor, fontWeight = FontWeight.Bold),
            text = "Face Detection", fontSize = 22.sp
        )

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            style = TextStyle(color = errorColor, fontWeight = FontWeight.Medium),
            text = message, fontSize = 14.sp
        )

    }
}


@Composable
fun Float.pxToDp(): Dp = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Int.pxToDp(): Dp = with(LocalDensity.current) { this@pxToDp.toFloat().toDp() }

@Composable
fun Dp.dpToPx(): Float = with(LocalDensity.current) { this@dpToPx.toPx() }