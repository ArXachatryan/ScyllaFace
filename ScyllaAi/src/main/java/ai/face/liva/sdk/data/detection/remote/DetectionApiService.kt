package ai.face.liva.sdk.data.detection.remote

import retrofit2.http.POST
import retrofit2.Response

interface DetectionApiService {

    @POST("checkLiveness")
    suspend fun checkLiveness(): Response<List<Any>>

    @POST("checkDocumentMatch")
    suspend fun checkDocumentMatch(): Response<List<Any>>
}
