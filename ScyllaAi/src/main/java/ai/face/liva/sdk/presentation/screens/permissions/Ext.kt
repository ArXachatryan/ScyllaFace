package ai.face.liva.sdk.presentation.screens.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

val Context.isCameraPermissionGranted: Boolean
    get() {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


val Activity?.shouldShowCameraPermissionRequiredCustomDialog: Boolean
    get() {
        if (this == null || this.isFinishing) return false

        return ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.CAMERA
        )
    }
