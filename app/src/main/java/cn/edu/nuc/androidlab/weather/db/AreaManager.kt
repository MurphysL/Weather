package cn.edu.nuc.androidlab.weather.db

import cn.edu.nuc.androidlab.weather.bean.City
import cn.edu.nuc.androidlab.weather.bean.Country
import cn.edu.nuc.androidlab.weather.bean.Province
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * AreaManager
 * Created by MurphySL on 2017/7/18.
 */
class AreaManager private constructor(){
    companion object {
        fun instance(): AreaManager = Inner.instance

        private object Inner {
            val instance = AreaManager()
        }
    }

    fun insertProvinces(provinces : List<Province>) =
            provinces.map {
                AreaDbHelper.instance.use {
                    insert(ProvinceTable.NAME,
                            ProvinceTable.PROVINCE_ID to it.id,
                            ProvinceTable.PROVINCE_NAME to it.name)
                }
            }

    fun queryProvince() : List<Province> =
            AreaDbHelper.instance.use {
                val rowParser = classParser<Province>()
                select(ProvinceTable.NAME).parseList(rowParser)
            }


    fun insertCity(citys : List<City>, province: Province) =
            citys.map {
                AreaDbHelper.instance.use {
                    insert(CityTable.NAME,
                            CityTable.CITY_ID to it.id,
                            CityTable.CITY_NAME to it.name,
                            CityTable.PROVINCE to province.id)
                }
            }

    fun queryCity(province_id : Int) : List<City> =
            AreaDbHelper.instance.use {
                val rowParser = classParser<City>()
                select(CityTable.NAME)
                        .whereArgs("(province = {province_id})", "province_id" to province_id)
                        .parseList(rowParser)
            }

    fun insertCountry(countries : List<Country>, city : City) =
            countries.map {
                AreaDbHelper.instance.use{
                    insert(CountryTable.NAME,
                            CountryTable.COUNTRY_ID to it.id,
                            CountryTable.COUNTRY_NAME to it.name,
                            CountryTable.WEAHTER_ID to it.weather_id,
                            CountryTable.CITY to city.id)
                }
            }

    fun queryCountry(city_id : Int) : List<Country> =
            AreaDbHelper.instance.use {
                val rowParser = classParser<Country>()
                select(CountryTable.NAME)
                        .whereArgs("(city={city_id})", "city_id" to city_id)
                        .parseList(rowParser)
            }

    fun queryCountryByWeatherId(weather_id : String) : List<Country> =
            AreaDbHelper.instance.use {
                val rowParser = classParser<Country>()
                select(CountryTable.NAME)
                        .whereArgs("(weather_id={weather_id})", "weather_id" to weather_id)
                        .parseList(rowParser)
            }
}