package ai.face.liva.sdk.di

import ai.face.liva.sdk.data.detection.remote.DetectionApiService
import ai.face.liva.sdk.network.base.ApiResponseHandel
import ai.face.liva.sdk.appHelper.constants.BaseUrl.BASE_URL
import com.google.android.datatransport.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single { provideLoggingInterceptor() }
    single { ApiResponseHandel()}
    single { provideBaseUrl() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get(), get()) }

    single { provideDetectionApiService(get()) }

}

private fun provideDetectionApiService(retrofit: Retrofit) = retrofit.create(DetectionApiService::class.java)


private fun provideBaseUrl(): String = BASE_URL

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}

private fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

