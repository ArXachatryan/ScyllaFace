package ai.face.liva.sdk.presentation.screens.detection.intent

import ai.face.liva.sdk.presentation.base.BaseIntent

sealed class DetectionIntent: BaseIntent {

    data object CheckLiveness : DetectionIntent()
    data object CheckDocumentMatch : DetectionIntent()
}