package cn.edu.nuc.androidlab.weather.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import cn.edu.nuc.androidlab.weather.App
import cn.edu.nuc.androidlab.weather.R
import cn.edu.nuc.androidlab.weather.adapter.ForecastAdapter
import cn.edu.nuc.androidlab.weather.bean.Weather
import cn.edu.nuc.androidlab.weather.db.AreaManager
import cn.edu.nuc.androidlab.weather.net.Service
import cn.edu.nuc.androidlab.weather.util.LocationManager
import com.amap.api.location.AMapLocation
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
    private var locationManager : LocationManager?= null
    lateinit var pref : SharedPreferences

    private var location_province : String = "北京"
    private var location_city : String = "北京"
    private var location_country : String = "朝阳"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val decorView : View = window.decorView
            decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        more.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        swipe.isRefreshing = true
        swipe.setOnRefreshListener {
            loadWeather()
        }

        loadBing()

        loadWeather()

        //initLocation()
    }

    private fun initLocation() {
        locationManager = LocationManager(App.instance)
        if(locationManager != null){
            locationManager?.setLocationListener(object : LocationManager.LocationListener {
                override fun onLocationChanged(aMapLocation: AMapLocation?) {
                    if(aMapLocation != null && aMapLocation.errorCode == 0){
                        castAreaName(aMapLocation)

                    }else {
                        Snackbar.make(more, "定位失败",Snackbar.LENGTH_INDEFINITE).show()
                    }
                }
            })

            locationManager?.openLocation()
        }
    }

    //地理位置不一 待修改
    private fun castAreaName(aMapLocation: AMapLocation){
        val p_len = aMapLocation.province.length
        location_province = aMapLocation.province.substring(0, p_len-1)

        val c_len = aMapLocation.city.length
        location_city = aMapLocation.city.substring(0, c_len-1)

        location_country = aMapLocation.district //区县一级命名混乱

        Log.i(TAG, "$location_country $location_city $location_province")
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

    private fun  showNow(now: Weather.HeWeather.Now) {
        with(now){
            degree.text = "$tmp℃"
            type.text = cond.txt
        }
    }

    private fun loadBing(){
        var bing = pref.getString("bing", null)
        if(bing == null){
            doAsync {
                val url = URL("http://guolin.tech/api/bing_pic")
                bing = url.readText()
                val editor : SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
                editor.putString("bing", bing)
                editor.apply()
                uiThread {
                    Log.i(TAG, bing)
                    Glide.with(context).load(bing).into(imageView)
                }
            }
        }else{
            Log.i(TAG, bing)
            Glide.with(context).load(bing).into(imageView)
        }

    }

    fun handleWeather(weather_id : String){
        location_country = AreaManager.instance().queryCountryByWeatherId(weather_id)[0].name

        tv_area.text = location_country

        Service.api_weather.getWeatherDetail(weather_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val weather_detail : Weather.HeWeather = it.HeWeather5[0]

                            if(weather_detail.status == "ok"){
                                showForecast(weather_detail.daily_forecast)
                                showAQI(weather_detail.aqi)
                                showSuggestion(weather_detail.suggestion)
                                showNow(weather_detail.now)

                                val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
                                editor.putString("country", location_country)
                                editor.putString("weather_id", weather_id)
                                editor.putString("weather", it.toString())
                                editor.apply()
                            }
                            swipe.isRefreshing = false
                        },
                        {
                            swipe.isRefreshing = false
                            Log.w(TAG,"loadWeather fail $it")
                        },
                        {
                            swipe.isRefreshing = false
                            Log.w(TAG,"loadCountry success")
                        }
                )
    }

    private fun loadWeather() {
        var weather_id = pref.getString("weather_id", null)

        if(weather_id == null){
            weather_id = intent.getStringExtra("weather_id")
        }
        Log.i(TAG, weather_id)
        handleWeather(weather_id)

    }

    override fun onDestroy() {
        super.onDestroy()
        if(locationManager != null)
            locationManager?.stopLocation()
        locationManager = null
    }

}