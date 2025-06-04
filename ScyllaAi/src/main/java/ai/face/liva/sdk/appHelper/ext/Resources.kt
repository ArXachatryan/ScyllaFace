package ai.face.liva.sdk.appHelper.ext

import ai.face.liva.sdk.appHelper.global.AppContextHolder.scyllaGlobalContext
import androidx.annotation.StringRes

fun getResString(@StringRes resId: Int): String = scyllaGlobalContext.getString(resId)

