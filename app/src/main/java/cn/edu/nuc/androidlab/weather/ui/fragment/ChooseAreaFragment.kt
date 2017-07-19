package cn.edu.nuc.androidlab.weather.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.nuc.androidlab.weather.R
import cn.edu.nuc.androidlab.weather.adapter.AreaAdapter
import cn.edu.nuc.androidlab.weather.adapter.MyClickListener
import cn.edu.nuc.androidlab.weather.bean.City
import cn.edu.nuc.androidlab.weather.bean.Country
import cn.edu.nuc.androidlab.weather.bean.Province
import cn.edu.nuc.androidlab.weather.db.AreaManager
import cn.edu.nuc.androidlab.weather.net.Service
import cn.edu.nuc.androidlab.weather.ui.activity.MainActivity
import cn.edu.nuc.androidlab.weather.ui.activity.WeatherActivity


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*
import java.util.*

/**
 * ChooseAreaFragment
 * Created by MurphySL on 2017/7/17.
 */
class ChooseAreaFragment : Fragment() {
    private val TAG : String = this.javaClass.simpleName

    private val PROVINCE_LEVEL = 0x001
    private val CITY_LEVEL = 0X002
    private val COUNTRY_LEVEL = 0x003

    private var curentLevel = 0x001
    private val data : ArrayList<String> = ArrayList()
    private val provinces : ArrayList<Province> = ArrayList()
    private val city : ArrayList<City> = ArrayList()
    private val country : ArrayList<Country> = ArrayList()

    private var selectProvince : Province?= null
    private var selectCity : City?= null

    private lateinit var toolbar : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : AreaAdapter

    private lateinit var manager : AreaManager

    private fun loadCity(){
        toolbar.title = selectProvince!!.name

        val list = manager.queryCity(selectProvince!!.id)
        if(list.isEmpty()){
            Service.api_area.getCity(selectProvince!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                city += it
                                it.forEach { data += it.name }
                                adapter.notifyDataSetChanged()
                                manager.insertCity(it, selectProvince!!)
                                Log.i(TAG, it.toString())
                            },
                            {
                                Log.w(TAG,"loadCity fail: $it")
                            },
                            {
                                Log.w(TAG,"loadCity success")
                            }
                    )
        }else{
            list.forEach {
                city += it
                data += it.name
                adapter.notifyDataSetChanged()
                Log.i(TAG, it.toString())
            }
        }

    }

    private fun loadCountry() {
        toolbar.title = selectCity!!.name

        val list = manager.queryCountry(selectCity!!.id)
        if(list.isEmpty()){
            Service.api_area.getCountry(selectProvince!!.id, selectCity!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                country += it
                                it.forEach { data += it.name }
                                adapter.notifyDataSetChanged()
                                manager.insertCountry(it, selectCity!!)
                                Log.i(TAG, it.toString())
                            },
                            {
                                Log.w(TAG,"loadCountry fail $it")
                            },
                            {
                                Log.w(TAG,"loadCountry success")
                            }
                    )
        }else{
            list.forEach {
                country += it
                data += it.name
                adapter.notifyDataSetChanged()
                Log.i(TAG, it.toString())
            }

        }

    }

    private fun loadProvince() {
        toolbar.title = resources.getString(R.string.select_province)

        val list = manager.queryProvince()
        if(list.isEmpty()){
            Service.api_area.getProvinces()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                provinces += it
                                it.forEach { data += it.name }
                                adapter.notifyDataSetChanged()
                                manager.insertProvinces(it)
                                Log.i(TAG, it.toString())
                            },
                            {
                                Log.w(TAG,"loadProvince fail $it")
                            },
                            {
                                Log.w(TAG,"loadProvince success")
                            }
                    )
        }else{
            list.forEach {
                provinces += it
                data += it.name
                adapter.notifyDataSetChanged()
                Log.i(TAG, it.toString())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = AreaManager.instance()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_choose_area, container, false)
        toolbar = rootView?.findViewById(R.id.toolbar)!!

        recyclerView = rootView.findViewById(R.id.recyclerView)!!
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = AreaAdapter(data)
        adapter.listener = object : MyClickListener {
            override fun onClick(position: Int) {
                data.clear()
                if(curentLevel == PROVINCE_LEVEL){
                    selectProvince = provinces[position]
                    loadCity()
                    curentLevel = CITY_LEVEL
                }else if(curentLevel == CITY_LEVEL && !city.isEmpty()){
                    selectCity = city[position]
                    loadCountry()
                    curentLevel = COUNTRY_LEVEL
                }else{
                    if(!country.isEmpty()){
                        if(activity is MainActivity){
                            val intent : Intent = Intent(context, WeatherActivity::class.java)
                            println(country[position].weather_id)
                            intent.putExtra("weather_id", country[position].weather_id)
                            startActivity(intent)
                        }else if( activity is WeatherActivity){
                            val a = activity as WeatherActivity
                            activity.drawer_layout.closeDrawers()
                            activity.swipe.isRefreshing = true
                            a.handleWeather(country[position].weather_id)
                        }
                    }
                }
            }
        }
        recyclerView.adapter = adapter

        loadProvince()

        return rootView
    }

}

