package cn.edu.nuc.androidlab.weather.bean

import java.io.Serializable

/**
 * 全部天气详细
 *
 * Created by MurphySL on 2017/7/17.
 */

data class Weather(var HeWeather5: List<HeWeather>) : Serializable{

    companion object {
        val serialVersionUID = 8711368828010083044L
    }

    data class HeWeather(var aqi: Aqi,
                          var basic: Basic,
                          var now: Now,
                          var status: String,
                          var suggestion: Suggestion,
                          var alarms: List<Alarms>,
                          var daily_forecast: List<DailyForecast>,
                          var hourly_forecast: List<HourlyForecast>)  : Serializable {
        data class Aqi (var city: City)  : Serializable {
            data class City(var aqi: String,
                            var co: String,
                            var no2: String,
                            var o3: String,
                            var pm10: String,
                            var pm25: String,
                            var qlty: String,
                            var so2: String) : Serializable
        }

        data class Basic (var city: String,
                         var cnty: String,
                         var id: String,
                         var lat: String,
                         var lon: String,
                         var prov: String,
                         var update: Update) : Serializable {
            data class Update (var loc: String,
                              var utc: String) : Serializable
        }

        data class Now (var cond: Cond,
                       var fl: String,
                       var hum: String,
                       var pcpn: String,
                       var pres: String,
                       var tmp: String,
                       var vis: String,
                       var wind: Wind) : Serializable {
            data class Cond (var code: String,
                            var txt: String) : Serializable

            data class Wind (var deg: String,
                            var dir: String,
                            var sc: String,
                            var spd: String) : Serializable
        }

        data class Suggestion(var comf: Comf,
                              var cw: Cw,
                              var drsg: Drsg,
                              var flu: Flu,
                              var sport: Sport,
                              var trav: Trav,
                              var uv: Uv) : Serializable {
            data class Comf(var brf: String,
                            var txt: String) : Serializable

            data class Cw(var brf: String,
                          var txt: String) : Serializable

            data class Drsg(var brf: String,
                            var txt: String) : Serializable

            data class Flu(var brf: String,
                           var txt: String) : Serializable

            data class Sport(var brf: String,
                             var txt: String) : Serializable

            data class Trav(var brf: String,
                            var txt: String) : Serializable

            data class Uv(var brf: String,
                          var txt: String) : Serializable
        }

        data class Alarms(var level: String,
                          var stat: String,
                          var title: String,
                          var txt: String,
                          var `type`: String) : Serializable

        data class DailyForecast(var astro: Astro,
                                 var cond: Cond,
                                 var date: String,
                                 var hum: String,
                                 var pcpn: String,
                                 var pop: String,
                                 var pres: String,
                                 var tmp: Tmp,
                                 var vis: String,
                                 var wind: Wind) : Serializable {
            data class Astro(var mr: String,
                             var ms: String,
                             var sr: String,
                             var ss: String) : Serializable

            data class Cond(var code_d: String,
                            var code_n: String,
                            var txt_d: String,
                            var txt_n: String) : Serializable

            data class Tmp(var max: String,
                           var min: String) : Serializable

            data class Wind(var deg: String,
                            var dir: String,
                            var sc: String,
                            var spd: String) : Serializable
        }

        data class HourlyForecast(var cond: Cond,
                                  var date: String,
                                  var hum: String,
                                  var pop: String,
                                  var pres: String,
                                  var tmp: String,
                                  var wind: Wind)  : Serializable{
            data class Cond(var code: String,
                            var txt: String) : Serializable

            data class Wind(var deg: String,
                            var dir: String,
                            var sc: String,
                            var spd: String) : Serializable
        }
    }
    
}