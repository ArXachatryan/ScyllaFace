package ai.face.liva.sdk.di

import ai.face.liva.sdk.domain.detection.useCase.DetectionUseCase
import ai.face.liva.sdk.domain.detection.useCaseImpl.DetectionUsersUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<DetectionUseCase> { DetectionUsersUseCaseImpl(get()) }
}