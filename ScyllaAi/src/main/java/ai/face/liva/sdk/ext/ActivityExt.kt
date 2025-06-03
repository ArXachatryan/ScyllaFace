package ai.face.liva.sdk.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

fun Context?.openLink(link: String?) {
    if (this == null || link.isNullOrEmpty()) return

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    this.startActivity(intent)
}