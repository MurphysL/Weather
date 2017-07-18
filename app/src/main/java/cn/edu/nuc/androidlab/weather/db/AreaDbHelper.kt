package cn.edu.nuc.androidlab.weather.db

import android.database.sqlite.SQLiteDatabase
import cn.edu.nuc.androidlab.weather.App
import org.jetbrains.anko.db.*

/**
 * AreaDbHelper
 * Created by MurphySL on 2017/7/18.
 */
class AreaDbHelper : ManagedSQLiteOpenHelper(App.instance, AreaDbHelper.DB_NAME, null, AreaDbHelper.DB_VERSION){

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable(ProvinceTable.NAME, true,
                ProvinceTable.PROVINCE_ID to INTEGER + PRIMARY_KEY,
                ProvinceTable.PROVINCE_NAME to TEXT)

        p0?.createTable(CityTable.NAME , true,
                CityTable.CITY_ID to INTEGER + PRIMARY_KEY,
                CityTable.CITY_NAME to TEXT,
                CityTable.PROVINCE to TEXT)

        p0?.createTable(CountryTable.NAME, true,
                CountryTable.COUNTRY_ID to INTEGER + PRIMARY_KEY,
                CountryTable.COUNTRY_NAME to TEXT,
                CountryTable.WEAHTER_ID to TEXT,
                CountryTable.CITY to TEXT)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.dropTable(ProvinceTable.NAME, true)
        p0?.dropTable(CityTable.NAME, true)
        p0?.dropTable(CountryTable.NAME, true)
    }

    companion object {
        val DB_NAME = "area.db"
        val DB_VERSION = 1
        val instance : AreaDbHelper by lazy { AreaDbHelper() }
    }
}