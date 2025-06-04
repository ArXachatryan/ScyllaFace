package ai.face.liva.sdk.app.liveness

import ai.face.liva.sdk.app.liveness.constants.FaceDetect.FACE_NOT_FOUND
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.FACE_TO_CENTER
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.HEED_DOWN
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.HEED_UP
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.MORE_THEN_ONE_FACE
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.MOVE_CAMERA_CLOSER
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.MOVE_CAMERA_FAR
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.OPEN_LEFT_EYES
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.OPEN_RIGHT_EYES
import ai.face.liva.sdk.app.liveness.constants.FaceDetect.OPEN_YOUR_EYES
import ai.face.liva.sdk.appHelper.global.AppContextHolder.scyllaGlobalContext
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class FaceLivenessAnalyzer(
    private val cameraOvalSize:Dp,
    private val onLivenessDetected: (ArrayList<Bitmap>) -> Unit,
    private val onError: (String) -> Unit,
    private val instructionMessage: (String) -> Unit,
) : ImageAnalysis.Analyzer {


    private val correctFaces:ArrayList<Bitmap> = ArrayList()

    private val faceDetector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()

        FaceDetection.getClient(options)
    }

    private var lastFace: Face? = null
    private var blinkCount = 0
    private var isProcessing = false

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        isProcessing = true
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )


            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    when{
                        faces.isEmpty()-> {
                            instructionMessage(FACE_NOT_FOUND)
                            resetLivenessCheck()
                        }
                        faces.size ==1->{
                            resetInstruction()
                            faces.first().also {

                                val faceZPosition =  it.boundingBox.height().dp
                                val faceZDiapason = cameraOvalSize-10.dp .. cameraOvalSize+100.dp

                                val headUpDownPosition = it.headEulerAngleX.toInt()
                                val headUpDownDiapason = -14 .. 14

                                val rightEye = it.rightEyeOpenProbability?:0f
                                val leftEyeEye = it.leftEyeOpenProbability?:0f

                                if (faceZPosition in faceZDiapason){
                                    resetInstruction()

                                    if (rightEye > 0.5 && leftEyeEye > 0.5){
                                        resetInstruction()
                                        if (headUpDownPosition in headUpDownDiapason){
                                            resetInstruction()

                                            val bitmap = mediaImage.toBitmap(imageProxy.imageInfo.rotationDegrees)
                                            val imageWidth = bitmap.width
                                            val imageHeight = bitmap.height

                                            val isFullFace = isFaceFullyInFrame(it, imageWidth, imageHeight)

                                            if (isFullFace) {
                                                resetInstruction()
//                                                val face = faces.first()
//                                                val faceBitmap = cropFaceFromBitmap(bitmap, face.boundingBox)
                                                correctFaces.add(bitmap)

                                                if (correctFaces.size == 9){
//                                                    correctFaces.map { face->
//
//                                                        saveBitmapToGallery(face)
//                                                    }

                                                    faceDetector.close()
                                                    onLivenessDetected(correctFaces)
                                                }
                                            } else {
                                                instructionMessage(FACE_TO_CENTER)

                                            }


                                        }else{

                                            if (headUpDownPosition < headUpDownDiapason.first){
                                                instructionMessage(HEED_DOWN)
                                            }else{
                                                instructionMessage(HEED_UP)
                                            }

                                        }
                                    }else{

                                        if (rightEye <= 0.5 && leftEyeEye <= 0.5){
                                            instructionMessage(OPEN_YOUR_EYES)
                                        }else if (rightEye <= 0.5){
                                            instructionMessage(OPEN_RIGHT_EYES)
                                        }else{
                                            instructionMessage(OPEN_LEFT_EYES)
                                        }

                                    }


                                }else{
                                    if (faceZPosition<faceZDiapason.start){
                                        instructionMessage(MOVE_CAMERA_CLOSER)
                                    }else{
                                        instructionMessage(MOVE_CAMERA_FAR)
                                    }

                                }

                            }

                        }
                        else->{
                            instructionMessage(MORE_THEN_ONE_FACE)

                            resetLivenessCheck()
                        }
                    }

                    imageProxy.close()
                    isProcessing = false
                }
                .addOnFailureListener { e ->
                    imageProxy.close()
                    isProcessing = false
                }
        } else {
            imageProxy.close()
            isProcessing = false
        }
    }

    private fun isFaceFullyInFrame(face: Face, imageWidth: Int, imageHeight: Int): Boolean {
        val rect = face.boundingBox

        return rect.left >= 0 &&
                rect.top >= 150 &&
                rect.right <= imageWidth &&
                rect.bottom <= (imageHeight*0.9)
    }

    private fun resetLivenessCheck() {
        blinkCount = 0
        correctFaces.clear()
        lastFace = null

    }
    private fun resetInstruction() {
        instructionMessage("")

    }
}

fun Image.toBitmap(rotationDegrees: Int): Bitmap {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
    val yuv = out.toByteArray()

    var bitmap = BitmapFactory.decodeByteArray(yuv, 0, yuv.size)
    if (rotationDegrees != 0) {
        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    return bitmap
}

fun cropFaceFromBitmap(source: Bitmap, rect: Rect): Bitmap? {
    return try {
        Bitmap.createBitmap(
            source,
            rect.left.coerceAtLeast(0),
            rect.top.coerceAtLeast(0),
            rect.width().coerceAtMost(source.width - rect.left),
            rect.height().coerceAtMost(source.height - rect.top)
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

//fun bitmapToFile(context: Context, bitmap: Bitmap, fileName: String = "face.jpg"): File {
//    val file = File(context.cacheDir, fileName)
//    file.outputStream().use { out ->
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
//    }
//    return file
//}

//val file = bitmapToFile(context, bitmap)
//val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//val body = MultipartBody.Part.createFormData("image", file.name, requestFile)


fun saveBitmapToGallery(bitmap: Bitmap) {
    CoroutineScope(Dispatchers.Default).launch {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "ScyllaAi_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val contentResolver = scyllaGlobalContext.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(it, contentValues, null, null)
        }
    }

}