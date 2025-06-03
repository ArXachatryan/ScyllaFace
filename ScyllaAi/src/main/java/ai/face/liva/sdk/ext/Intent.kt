package ai.face.liva.sdk.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.launchActivity(
    noinline intentBuilder: (Intent.() -> Unit)? = null
) {
    val intent = Intent(this, T::class.java)
    intentBuilder?.let { intent.it() }
    startActivity(intent)
}

