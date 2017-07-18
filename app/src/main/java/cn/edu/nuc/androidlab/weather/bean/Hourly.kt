package cn.edu.nuc.androidlab.weather.bean

/**
 * Created by MurphySL on 2017/7/18.
 */
data class Hourly(var HeWeather5: List<HeWeather>) {

    data class HeWeather(var basic: Basic,
                          var status: String,
                          var hourly_forecast: List<HourlyForecast>) {
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