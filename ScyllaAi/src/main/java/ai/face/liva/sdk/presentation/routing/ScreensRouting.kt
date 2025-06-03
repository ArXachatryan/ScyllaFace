package ai.face.liva.sdk.presentation.routing


sealed class ScreensRouting(val route: String) {
    data object Consent : ScreensRouting("Consent")
    data object UploadDocument : ScreensRouting("Upload Document")
    data object LiveDetection : ScreensRouting("Detection")
    data object AnalyzingScreen : ScreensRouting("Analyzing")
}