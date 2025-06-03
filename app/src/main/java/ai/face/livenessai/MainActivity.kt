package ai.face.livenessai

import ai.face.liva.sdk.ScyllaAiDetectionType
import ai.face.liva.sdk.sdkBuilder.DetectionResultType
import ai.face.liva.sdk.sdkBuilder.ScyllaAI
import ai.face.liva.sdk.sdkBuilder.ScyllaAiConfiguration
import ai.face.liva.sdk.ui.theme.appBackgroundColor
import ai.face.liva.sdk.ui.theme.appLightColor
import ai.face.liva.sdk.ui.theme.appStyleColor
import ai.face.livenessai.ui.theme.LivenessAITheme
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivenessAITheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                SimpleScreen(
                    modifier = Modifier.padding(innerPadding),
                    activity = this
                )
            }
            }
        }

    }
}

var resultMessage  =  mutableStateOf("")

    @Composable
fun SimpleScreen(modifier: Modifier = Modifier, activity: Activity) {


    Column(modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = modifier.weight(1f))

        Text(
            style = TextStyle(color = appLightColor, fontWeight = FontWeight.Bold),
            text = "Init ScyllaAI SDK", fontSize = 22.sp)
        Spacer(modifier = modifier.weight(1f))

        Text(
            style = TextStyle(color = appStyleColor, fontWeight = FontWeight.Bold),
            text = resultMessage.value, fontSize = 22.sp)

        Spacer(modifier = modifier.weight(1f))
        Button(
            onClick = {
                initSdk(activity = activity,imitation = "SUCCESS")
            },
            modifier = modifier.fillMaxWidth().height(50.dp),
        ) {
            Text(
                style = TextStyle(color = appBackgroundColor, fontWeight = FontWeight.Bold),
                text = "SUCCESS IMITATION", fontSize = 16.sp)
        }
        Button(
            onClick = {
                initSdk(activity = activity, imitation = "ERROR")
            },
            modifier = modifier.fillMaxWidth().height(50.dp),
        ) {
            Text(
                style = TextStyle(color = appBackgroundColor, fontWeight = FontWeight.Bold),
                text = "ERROR IMITATION", fontSize = 16.sp)
        }


    }

}

fun initSdk(activity:Activity,imitation:String){
    resultMessage.value = ""
    ScyllaAI.Builder(activity = activity,imitation = imitation)
        .configuration(
            ScyllaAiConfiguration(
                accessKey = "YOUR ACCESS YEY",
                detectionType = ScyllaAiDetectionType.LIVENESS,
                showConsentScreen = true,
                showSuccessScreen = false,
                showErrorScreen = true

            )
        )
        .resultCallBack { result->
            resultMessage.value = result.message
            when(result.resultType){
                DetectionResultType.INVALID_ACCESS_TOKEN -> {}
                DetectionResultType.DETECTION_SUCCESS -> {}
                DetectionResultType.DETECTION_ERROR ->{}
                DetectionResultType.ERROR ->{}
            }

        }
        .build()

}
