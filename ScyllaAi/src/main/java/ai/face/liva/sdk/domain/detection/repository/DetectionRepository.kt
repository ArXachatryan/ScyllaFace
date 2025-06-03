package ai.face.liva.sdk.domain.detection.repository
import ai.face.liva.sdk.network.base.ResponseState
import kotlinx.coroutines.flow.Flow


interface DetectionRepository {

        suspend fun checkLiveness(): Flow<ResponseState<Any>>
        suspend fun checkDocumentMatch(): Flow<ResponseState<Any>>
}