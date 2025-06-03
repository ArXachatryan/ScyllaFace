package ai.face.liva.sdk.presentation.screens.uploadDocument

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

enum class UploadScreenState {
    SELECT_TYPE, UPLOAD_DOCUMENT, CAPTURE_IMAGE
}

@Composable
fun UploadFlowScreen(
    navController: NavController
) {
    var currentScreen by remember { mutableStateOf(UploadScreenState.SELECT_TYPE) }
    var selectedDocumentUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    when (currentScreen) {
        UploadScreenState.SELECT_TYPE -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { currentScreen = UploadScreenState.UPLOAD_DOCUMENT },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload Document")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { currentScreen = UploadScreenState.CAPTURE_IMAGE },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Capture Image")
                }
            }
        }

        UploadScreenState.UPLOAD_DOCUMENT -> {
            DocumentUploadScreen(
                onDocumentSelected = { uri ->
                    selectedDocumentUri = uri
                    currentScreen = UploadScreenState.SELECT_TYPE
                },
                onBack = { currentScreen = UploadScreenState.SELECT_TYPE }
            )
        }

        UploadScreenState.CAPTURE_IMAGE -> {
//            ImageCaptureScreen(
//                onImageCaptured = { uri ->
//                    selectedImageUri = uri
//                    currentScreen = UploadScreenState.SELECT_TYPE
//                },
//                onBack = { currentScreen = UploadScreenState.SELECT_TYPE }
//            )
//        }
        }
    }}