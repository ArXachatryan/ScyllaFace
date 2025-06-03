package ai.face.liva.sdk.domain.detection.useCase

import ai.face.liva.sdk.network.base.ResponseState
import kotlinx.coroutines.flow.Flow


interface DetectionUseCase  {

    suspend fun checkLiveness(): Flow<ResponseState<Any>>
    suspend fun checkDocumentMatch(): Flow<ResponseState<Any>>

}