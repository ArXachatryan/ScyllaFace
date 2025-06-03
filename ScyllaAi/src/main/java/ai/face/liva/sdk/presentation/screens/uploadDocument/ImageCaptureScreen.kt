package ai.face.liva.sdk.presentation.screens.uploadDocument

import ai.face.liva.R
import ai.face.liva.sdk.presentation.component.MainButton
import ai.face.liva.sdk.presentation.component.MainOutlinedButton
import ai.face.liva.sdk.presentation.routing.ScreensRouting
import ai.face.liva.sdk.ui.theme.appDimColorLight
import ai.face.liva.sdk.ui.theme.appInactiveColor
import ai.face.liva.sdk.ui.theme.cardShape
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageCaptureScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val context = LocalContext.current
    var capturedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var uploadedImage by rememberSaveable { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) capturedImageUri?.let{

                uploadedImage = it
            }
        }
    )
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let{
            uploadedImage = it
          }
        }
    )

    // Camera permission
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Log.e("scdvdvdvdv", "cxxxxxxxxxxxxx")
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(cardShape)
                .height(250.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                .border(1.dp, appDimColorLight, shape = cardShape),
            contentAlignment = Alignment.Center
        ) {
            if (uploadedImage != null) {
                Image(
                    painter = rememberAsyncImagePainter(uploadedImage),
                    contentDescription = "Captured image",
                    modifier = modifier.fillMaxSize()
                        .clip(cardShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = modifier.size(70.dp),
                        painter = painterResource(R.drawable.empty_image_icon),
                        colorFilter = ColorFilter.tint(appInactiveColor),
                        contentDescription = null)

                }
            }
        }

        Spacer(modifier = modifier.height(50.dp))

        // Action buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            MainOutlinedButton(
                modifier = modifier,
                name = "Take Photo",
                startIcon = R.drawable.camera_icon

            ){
                if (cameraPermissionState.status.isGranted) {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        File.createTempFile("IMG_${System.currentTimeMillis()}", ".jpg", context.cacheDir)
                    )
                    capturedImageUri = uri
                    cameraLauncher.launch(uri)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            }

            MainOutlinedButton(
                modifier = modifier,
                name = "Choose from Gallery",
                startIcon = R.drawable.gallery_icon
            ){
                galleryLauncher.launch("image/*")

            }

        }

        Spacer(modifier = modifier.weight(1f))

        MainButton(
            enabled = uploadedImage!=null,
            modifier = modifier,
            name = "Continue",

            ){
            navController.navigate(ScreensRouting.LiveDetection.route)
        }
    }
}