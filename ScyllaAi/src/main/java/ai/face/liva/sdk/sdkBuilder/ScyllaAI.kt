package ai.face.liva.sdk.sdkBuilder

import ai.face.liva.sdk.Scylla
import ai.face.liva.sdk.appHelper.ext.isNull
import ai.face.liva.sdk.ext.launchActivity
import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


enum class DetectionResultType {

     INVALID_ACCESS_TOKEN, DETECTION_SUCCESS, DETECTION_ERROR, ERROR

}

data class DetectionResult(

    val resultType: DetectionResultType = DetectionResultType.ERROR,
    val message: String = ""

)

suspend fun setDetectionResult(resultType: DetectionResultType, message: String) {

    result.send(DetectionResult(resultType = resultType, message = message))

}

internal val result = Channel<DetectionResult>(Channel.UNLIMITED)

var resultImitation = "SUCCESS"

class ScyllaAI {

    class Builder(private val activity: Activity,private val imitation:String) {

        init {
            configParams = null
            resultImitation = imitation
        }

        fun configuration(params: ScyllaAiConfiguration) = apply { configParams = params }

         fun resultCallBack(message: (DetectionResult) -> Unit) = apply {
            val detectionResult = result.receiveAsFlow()
            CoroutineScope(Dispatchers.IO).launch {
                detectionResult.collect{ result->
                    message(result)
                }
            }

        }


        fun build() {

            configParams.isNull {

                CoroutineScope(Dispatchers.IO).launch {
                    setDetectionResult(resultType = DetectionResultType.ERROR,"Pls setup Configuration Params")
                }

            }?.let {
                activity.launchActivity<Scylla> { putExtra("CONFIG", it) }
            }

        }
    }
}

