package ai.face.liva.sdk.network.base

import ai.face.liva.R
import ai.face.liva.sdk.appHelper.ext.getResString


sealed class ResponseState<out T> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class Error(val message: String? = getResString(R.string.SomethingWentWrong)) : ResponseState<Nothing>()
    data object Loading : ResponseState<Nothing>()
}