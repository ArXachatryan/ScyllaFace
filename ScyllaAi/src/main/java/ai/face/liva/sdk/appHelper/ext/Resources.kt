package ai.face.liva.sdk.appHelper.ext

import ai.face.liva.sdk.appHelper.global.AppContextHolder.globalContext
import androidx.annotation.StringRes

fun getResString(@StringRes resId: Int): String = globalContext.getString(resId)

