package cn.edu.nuc.androidlab.weather

import android.app.Application
import kotlin.properties.Delegates

/**
 * App
 * Created by MurphySL on 2017/7/18.
 */
class App : Application(){
    companion object {
        var instance : Application by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}