package ai.face.liva.sdk

import ai.face.liva.sdk.appHelper.global.AppContextHolder
import ai.face.liva.sdk.di.initKoin
import android.app.Application

open class ScyllaApp  {

    fun initScyllaApp(application: Application) {
        try {
           initKoin(application)
        }catch (e:Exception){}

        AppContextHolder.scyllaGlobalContext = application.applicationContext
    }

    fun testTest() {


    }
}

