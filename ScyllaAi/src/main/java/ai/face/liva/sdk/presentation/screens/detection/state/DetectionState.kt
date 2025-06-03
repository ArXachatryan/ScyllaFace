package ai.face.liva.sdk.presentation.screens.detection.state

import ai.face.liva.sdk.network.base.ResponseState
import ai.face.liva.sdk.presentation.base.BaseStateI


data class DetectionState(
    val livenessResult: ResponseState<Any> = ResponseState.Loading,
    val documentMatchResult: ResponseState<Any> = ResponseState.Loading
): BaseStateI