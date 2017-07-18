package cn.edu.nuc.androidlab.weather

import android.app.Application

/**
 * App
 * Created by MurphySL on 2017/7/18.
 */
class App : Application(){
    companion object {
        private var instance : App? = null
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}