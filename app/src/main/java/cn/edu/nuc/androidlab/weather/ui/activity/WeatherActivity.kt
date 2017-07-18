package cn.edu.nuc.androidlab.weather.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import cn.edu.nuc.androidlab.weather.R
import cn.edu.nuc.androidlab.weather.adapter.ForecastAdapter
import cn.edu.nuc.androidlab.weather.bean.Weather
import cn.edu.nuc.androidlab.weather.service.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * WeatherActivity
 * Created by MurphySL on 2017/7/17.
 */
class WeatherActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        comf.text = "${resources.getString(R.string.comf)}${suggestion.comf}"
        drsg.text = "${resources.getString(R.string.drsg)}${suggestion.drsg}"
        flu.text = "${resources.getString(R.string.flu)}${suggestion.flu}"
        sport.text = "${resources.getString(R.string.sport)}${suggestion.sport}"
        trav.text = "${resources.getString(R.string.trv)}${suggestion.trav}"
        uv.text = "${resources.getString(R.string.uv)}${suggestion.uv}"
    }

    private fun getWeather(area : String) {
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


}