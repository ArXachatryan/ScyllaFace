package ai.face.liva.sdk.network.base

import com.google.gson.annotations.SerializedName


data class ErrorResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("statusCode")
    val statusCode: Int = 0,

    @SerializedName("desc")
    val description: String? = null
)