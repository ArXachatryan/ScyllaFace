package ai.face.liva.sdk.network.base

import ai.face.liva.R
import ai.face.liva.sdk.appHelper.ext.getResString
import android.accounts.NetworkErrorException
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow
import retrofit2.Response


class ApiResponseHandel {
    fun <T> safeApiCall(apiCall: suspend () -> Response<T>) = flow {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit( ResponseState.Success<T>(body))
                } else {
                    throw NullPointerException(getResString(R.string.SomethingWentWrong))
                }
            } else {

                val string: String? = response.errorBody()?.string()
                val errorData: ErrorResponse = Gson().fromJson(string, ErrorResponse::class.java)
                val errMessage = errorData.message ?: errorData.description

                throw RuntimeException(errMessage?:getResString(R.string.SomethingWentWrong))

            }

        } catch (e: Exception) {

            throw NetworkErrorException(getResString(R.string.NoInternetConnection))

        }
    }
}
