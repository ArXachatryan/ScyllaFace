package ai.face.liva.sdk.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initKoin(application: Application) {
    startKoin {
        androidContext(application)
        modules(listOf(
            networkModule,
            repositoryModule,
            useCaseModule,
            viewModelModule,
        ))
    }
}
