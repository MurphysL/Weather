package cn.edu.nuc.androidlab.weather.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log
import cn.edu.nuc.androidlab.weather.bean.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import java.net.URL

/**
 * AutoUpdateService
 * Created by MurphySL on 2017/7/19.
 */
class AutoUpdateService : Service(){
    private val TAG : String = this.javaClass.simpleName
    private val HOUR : Int = 8 * 60 * 60 * 1000
    private val context = this

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //updateWeather()
        updateBing()
        val manager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAtTime = SystemClock.elapsedRealtime() + HOUR
        val i = Intent(this, AutoUpdateService::class.java)
        val pi = PendingIntent.getService(this, 0, i, 0)
        manager.cancel(pi)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateBing() {
        doAsync {
            val url = URL("http://guolin.tech/api/bing_pic")
            val bing = url.readText()
            val editor : SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putString("bing", bing)
            editor.apply()
        }
    }

    private fun updateWeather() {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val weather_id = pref.getString("weather_id", null)
        if(weather_id != null){
            cn.edu.nuc.androidlab.weather.net.Service.api_weather.getWeatherDetail(weather_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                val weather_detail : Weather.HeWeather = it.HeWeather5[0]

                                if(weather_detail.status == "ok"){
                                    val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
                                    editor.putString("weather", it.toString())
                                    editor.apply()
                                }
                            },
                            {
                                Log.w(TAG,"updateWeather fail $it")
                            },
                            {
                                Log.w(TAG,"updateWeather success")
                            }
                    )
        }
    }

}