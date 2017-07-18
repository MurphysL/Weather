package cn.edu.nuc.androidlab.weather.service

import cn.edu.nuc.androidlab.weather.bean.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Service
 * Created by MurphySL on 2017/7/17.
 */
object Service{
    val api_weather : HeWeatherService by lazy {
        Retrofit.Builder()
                .baseUrl("https://free-api.heweather.com/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(HeWeatherService::class.java)
    }

    val api_area: AreaService by lazy {
        Retrofit.Builder()
                .baseUrl("http://guolin.tech/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AreaService::class.java)
    }

}

interface AreaService {
    @GET("china")
    fun getProvinces() : Observable<List<Province>>

    @GET("china/{province_id}")
    fun getCity(@Path("province_id") city_id : Int?) : Observable<List<City>>

    @GET("china/{province_id}/{city_id}")
    fun getCountry(@Path("province_id") city_id : Int?, @Path("city_id") country_id : Int?) : Observable<List<Country>>
}

private const val KEY : String = "db60a26e5d764427a0de9a179da2d18b"

interface HeWeatherService{
    @GET("weather?key=$KEY")
    fun getWeatherDetail(@Query("city") city : String) : Observable<Weather>

    @GET("hourly?key=$KEY")
    fun getHourly(@Query("city") city : String) : Observable<Hourly>
}