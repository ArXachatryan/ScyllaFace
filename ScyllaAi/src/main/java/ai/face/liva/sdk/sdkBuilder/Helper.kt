package ai.face.liva.sdk.sdkBuilder

import ai.face.liva.sdk.ScyllaAiDetectionType
import android.annotation.SuppressLint
import java.io.Serializable


data class ScyllaAiConfiguration(

    val accessKey:String = "",
    val detectionType:ScyllaAiDetectionType = ScyllaAiDetectionType.LIVENESS,
    val showConsentScreen:Boolean = true,
    val showSuccessScreen:Boolean = true,
    val showErrorScreen:Boolean = true,

    ) : Serializable

@SuppressLint("StaticFieldLeak")
internal var configParams:ScyllaAiConfiguration? = null



