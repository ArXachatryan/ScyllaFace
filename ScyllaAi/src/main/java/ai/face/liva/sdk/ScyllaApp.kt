package ai.face.liva.sdk

import ai.face.liva.sdk.appHelper.global.AppContextHolder
import ai.face.liva.sdk.di.initKoin
import android.app.Application

 class ScyllaApp:Application()  {

     override fun onCreate() {
         super.onCreate()
         initKoin(this)
         AppContextHolder.scyllaGlobalContext = applicationContext
     }

     fun test3(){

     }
}

