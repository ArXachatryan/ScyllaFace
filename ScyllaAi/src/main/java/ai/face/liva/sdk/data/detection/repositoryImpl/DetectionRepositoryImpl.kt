package ai.face.liva.sdk.data.detection.repositoryImpl

import ai.face.liva.sdk.data.detection.remote.DetectionApiService
import ai.face.liva.sdk.domain.detection.repository.DetectionRepository
import ai.face.liva.sdk.network.base.ApiResponseHandel
import ai.face.liva.sdk.network.base.ResponseState
import ai.face.liva.sdk.sdkBuilder.resultImitation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class DetectionRepositoryImpl(
    private val apiService: DetectionApiService,
    private val dataSource: ApiResponseHandel,
) : DetectionRepository {


    override suspend fun checkLiveness() = flow {

        kotlinx.coroutines.delay(3000)

        if (resultImitation == "SUCCESS"){
            emit(ResponseState.Success( "Success"))
        }else{
            emit(ResponseState.Error( "Error"))

        }



//        dataSource.safeApiCall{apiService.checkLiveness()}
//            .catch { emit(ResponseState.Error(it.message)) }
//            .collect{
//                emit(ResponseState.Success( it.data))
//
//            }

    }.flowOn(Dispatchers.IO)

    override suspend fun checkDocumentMatch() = flow {


        dataSource.safeApiCall{apiService.checkDocumentMatch()}
            .catch { emit(ResponseState.Error(it.message)) }
            .collect{
                emit(ResponseState.Success( it.data))

            }

    }.flowOn(Dispatchers.IO)



}