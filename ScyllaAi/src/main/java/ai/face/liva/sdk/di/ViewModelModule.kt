package ai.face.liva.sdk.di

import ai.face.liva.sdk.presentation.screens.detection.viewModel.DetectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ DetectionViewModel(get()) }
}