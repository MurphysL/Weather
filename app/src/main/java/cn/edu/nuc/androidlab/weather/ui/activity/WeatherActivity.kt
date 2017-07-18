package cn.edu.nuc.androidlab.weather.ui.activity

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import cn.edu.nuc.androidlab.weather.R
import cn.edu.nuc.androidlab.weather.adapter.ForecastAdapter
import cn.edu.nuc.androidlab.weather.bean.Weather
import cn.edu.nuc.androidlab.weather.service.Service
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * WeatherActivity
 * Created by MurphySL on 2017/7/17.
 */
class WeatherActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName
    private val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= 21){
            val decorView : View = window.decorView
            decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)

        val area = intent.getStringExtra("country_code")

        getWeather(area)
    }

    private fun showForecast(forecast: List<Weather.HeWeather.DailyForecast>) {
        daily_forecast.layoutManager = LinearLayoutManager(this)
        daily_forecast.adapter = ForecastAdapter(this, forecast)
    }

    private fun showAQI(aqi_info : Weather.HeWeather.Aqi){
        aqi.text = aqi_info.city.aqi
        pm25.text = aqi_info.city.pm25
    }

    private fun showSuggestion(suggestion : Weather.HeWeather.Suggestion){
        comf.text = "${resources.getString(R.string.comf)}${suggestion.comf.txt}"
        drsg.text = "${resources.getString(R.string.drsg)}${suggestion.drsg.txt}"
        flu.text = "${resources.getString(R.string.flu)}${suggestion.flu.txt}"
        sport.text = "${resources.getString(R.string.sport)}${suggestion.sport.txt}"
        trav.text = "${resources.getString(R.string.trv)}${suggestion.trav.txt}"
        uv.text = "${resources.getString(R.string.uv)}${suggestion.uv.txt}"
    }

    private fun getWeather(area : String) {
        doAsync {
            val url = URL("http://guolin.tech/api/bing_pic")
            val s = url.readText()
            uiThread {
                Glide.with(context).load(s).into(imageView)
            }
        }
        Service.api_weather.getWeatherDetail(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val weather_deatil : Weather.HeWeather = it.HeWeather5[0]

                            if(weather_deatil.status == "ok"){
                                showForecast(weather_deatil.daily_forecast)
                                showAQI(weather_deatil.aqi)
                                showSuggestion(weather_deatil.suggestion)
                                showNow(weather_deatil.now)
                            }
                        },
                        {
                            Log.w(TAG,"getWeather fail $it")
                        },
                        {
                            Log.w(TAG,"loadCountry success")
                        }
                )
    }

    private fun  showNow(now: Weather.HeWeather.Now) {
        with(now){
            degree.text = "$tmp℃"
            type.text = cond.txt
        }
    }


}