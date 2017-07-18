package cn.edu.nuc.androidlab.weather

import cn.edu.nuc.androidlab.weather.service.Service
import org.junit.Test

/**
 * HeWeatherTest
 * Created by MurphySL on 2017/7/18.
 */
class HeWeatherTest{
    @Test
    fun weatherTest(){
        Service.api_weather.getWeatherDetail("beijing").subscribe {
            println(it)
        }
    }

    @Test
    fun hourlyTest(){
        Service.api_weather.getHourly("beijing").subscribe {
            println(it.toString())
        }
    }
}