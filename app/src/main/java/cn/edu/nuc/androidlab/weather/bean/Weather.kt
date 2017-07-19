package cn.edu.nuc.androidlab.weather.bean

/**
 * 全部天气详细
 *
 * Created by MurphySL on 2017/7/17.
 */

data class Weather(var HeWeather5: List<HeWeather>) {

    data class HeWeather(var aqi: Aqi,
                          var basic: Basic,
                          var now: Now,
                          var status: String,
                          var suggestion: Suggestion,
                          var alarms: List<Alarms>,
                          var daily_forecast: List<DailyForecast>,
                          var hourly_forecast: List<HourlyForecast>) {
        data class Aqi(var city: City) {
            data class City(var aqi: String,
                            var co: String,
                            var no2: String,
                            var o3: String,
                            var pm10: String,
                            var pm25: String,
                            var qlty: String,
                            var so2: String)
        }

        data class Basic(var city: String,
                         var cnty: String,
                         var id: String,
                         var lat: String,
                         var lon: String,
                         var prov: String,
                         var update: Update) {
            data class Update(var loc: String,
                              var utc: String)
        }

        data class Now(var cond: Cond,
                       var fl: String,
                       var hum: String,
                       var pcpn: String,
                       var pres: String,
                       var tmp: String,
                       var vis: String,
                       var wind: Wind) {
            data class Cond(var code: String,
                            var txt: String)

            data class Wind(var deg: String,
                            var dir: String,
                            var sc: String,
                            var spd: String)
        }

        data class Suggestion(var comf: Comf,
                              var cw: Cw,
                              var drsg: Drsg,
                              var flu: Flu,
                              var sport: Sport,
                              var trav: Trav,
                              var uv: Uv) {
            data class Comf(var brf: String,
                            var txt: String)

            data class Cw(var brf: String,
                          var txt: String)

            data class Drsg(var brf: String,
                            var txt: String)

            data class Flu(var brf: String,
                           var txt: String)

            data class Sport(var brf: String,
                             var txt: String)

            data class Trav(var brf: String,
                            var txt: String)

            data class Uv(var brf: String,
                          var txt: String)
        }

        data class Alarms(var level: String,
                          var stat: String,
                          var title: String,
                          var txt: String,
                          var `type`: String)

        data class DailyForecast(var astro: Astro,
                                 var cond: Cond,
                                 var date: String,
                                 var hum: String,
                                 var pcpn: String,
                                 var pop: String,
                                 var pres: String,
                                 var tmp: Tmp,
                                 var vis: String,
                                 var wind: Wind) {
            data class Astro(var mr: String,
                             var ms: String,
                             var sr: String,
                             var ss: String)

            data class Cond(var code_d: String,
                            var code_n: String,
                            var txt_d: String,
                            var txt_n: String)

            data class Tmp(var max: String,
                           var min: String)

            data class Wind(var deg: String,
                            var dir: String,
                            var sc: String,
                            var spd: String)
        }

        data class HourlyForecast(var cond: Cond,
                                  var date: String,
                                  var hum: String,
                                  var pop: String,
                                  var pres: String,
                                  var tmp: String,
                                  var wind: Wind) {
            data class Cond(var code: String,
                            var txt: String)

            data class Wind(var deg: String,
                            var dir: String,
                            var sc: String,
                            var spd: String)
        }
    }
    
}