package ai.face.liva.sdk.presentation.screens.detection.viewModel

import ai.face.liva.sdk.domain.detection.useCase.DetectionUseCase
import ai.face.liva.sdk.network.base.ResponseState
import ai.face.liva.sdk.presentation.base.BaseMviViewModel
import ai.face.liva.sdk.presentation.screens.detection.intent.DetectionIntent
import ai.face.liva.sdk.presentation.screens.detection.state.DetectionState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

class DetectionViewModel(private val detectionUseCase: DetectionUseCase) : BaseMviViewModel<DetectionIntent, DetectionState>() {

    init { handleIntents() }

    override val initialState: DetectionState get() = DetectionState()


    private fun handleIntents() {
        launchOnViewModel {
            intent.collect { intent ->
                when (intent) {
                    is DetectionIntent.CheckLiveness -> checkLiveness()
                    is DetectionIntent.CheckDocumentMatch -> checkDocumentMatch()
                }
            }
        }
    }

    private fun checkLiveness() {

        launchOnViewModel {
            detectionUseCase.checkLiveness()
                .onStart { processState(state.value.copy(livenessResult = ResponseState.Loading))}
                .catch { processState(state.value.copy(livenessResult = ResponseState.Error())) }
                .collect { apiRespState ->

                    processState(state.value.copy(livenessResult = apiRespState))
                }

        }
    }
    private fun checkDocumentMatch() {

        launchOnViewModel {
            detectionUseCase.checkDocumentMatch()
                .onStart { processState(state.value.copy(documentMatchResult = ResponseState.Loading))}
                .catch { processState(state.value.copy(documentMatchResult = ResponseState.Error())) }
                .collect { apiRespState -> processState(state.value.copy(documentMatchResult = apiRespState))}

        }
    }




}