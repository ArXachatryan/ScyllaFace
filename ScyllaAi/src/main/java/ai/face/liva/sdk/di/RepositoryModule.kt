package ai.face.liva.sdk.di


import ai.face.liva.sdk.data.detection.repositoryImpl.DetectionRepositoryImpl
import ai.face.liva.sdk.domain.detection.repository.DetectionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<DetectionRepository> { DetectionRepositoryImpl(apiService =get(),dataSource = get ()) }
}
