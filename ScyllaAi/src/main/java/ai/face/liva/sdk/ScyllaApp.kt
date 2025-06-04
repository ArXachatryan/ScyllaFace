package ai.face.liva.sdk

import ai.face.liva.sdk.appHelper.global.AppContextHolder
import ai.face.liva.sdk.di.initKoin
import android.app.Application

 class ScyllaApp  {

        fun initScyllaApp(application: Application) {
            initKoin(application)
            AppContextHolder.scyllaGlobalContext = application.applicationContext
       }
}