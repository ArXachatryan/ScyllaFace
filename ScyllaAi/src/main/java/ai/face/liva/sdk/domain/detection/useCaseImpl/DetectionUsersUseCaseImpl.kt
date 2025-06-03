
package ai.face.liva.sdk.domain.detection.useCaseImpl

import ai.face.liva.sdk.domain.detection.repository.DetectionRepository
import ai.face.liva.sdk.domain.detection.useCase.DetectionUseCase
import ai.face.liva.sdk.network.base.ResponseState
import kotlinx.coroutines.flow.Flow

class DetectionUsersUseCaseImpl(private val repository: DetectionRepository):DetectionUseCase {

    override suspend fun checkLiveness(): Flow<ResponseState<Any>>  = repository.checkLiveness()
    override suspend fun checkDocumentMatch(): Flow<ResponseState<Any>> = repository.checkDocumentMatch()


}