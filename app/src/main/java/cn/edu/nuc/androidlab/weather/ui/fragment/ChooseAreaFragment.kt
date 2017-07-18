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
import cn.edu.nuc.androidlab.weather.service.Service
import cn.edu.nuc.androidlab.weather.ui.activity.WeatherActivity


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    private fun loadCity(){
        Service.api_area.getCity(selectProvince!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                {
                    toolbar.title = selectProvince!!.name
                    city += it
                    it.forEach { data += it.name }
                    adapter.notifyDataSetChanged()
                    Log.i(TAG, it.toString())
                },
                {
                    Log.w(TAG,"loadCity fail: $it")
                },
                {
                    Log.w(TAG,"loadCity success")
                }
        )
    }

    private fun loadCountry() {
        Service.api_area.getCountry(selectProvince!!.id, selectCity!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                {
                    toolbar.title = selectCity!!.name
                    country += it
                    it.forEach { data += it.name }
                    adapter.notifyDataSetChanged()
                    Log.i(TAG, it.toString())
                },
                {
                    Log.w(TAG,"loadCountry fail $it")
                },
                {
                    Log.w(TAG,"loadCountry success")
                }
        )
    }

    private fun loadProvince() {
        Service.api_area.getProvinces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                {
                    toolbar.title = resources.getString(R.string.select_province)
                    provinces += it
                    it.forEach { data += it.name }
                    adapter.notifyDataSetChanged()
                    Log.i(TAG, it.toString())
                },
                {
                    Log.w(TAG,"loadProvince fail $it")
                },
                {
                    Log.w(TAG,"loadProvince success")
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_choose_area, container, false)
        toolbar = rootView?.findViewById(R.id.toolbar)!!
        recyclerView = rootView.findViewById(R.id.recyclerView)!!
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        adapter = AreaAdapter(context, data)
        adapter.listener = object : MyClickListener {
            override fun onClick(position: Int) {
                data.clear()
                if(curentLevel == PROVINCE_LEVEL){
                    selectProvince = provinces[position]
                    loadCity()
                    curentLevel = CITY_LEVEL
                }else if(curentLevel == CITY_LEVEL){
                    selectCity = city[position]
                    loadCountry()
                    curentLevel = COUNTRY_LEVEL
                }else{
                    val intent : Intent = Intent(context, WeatherActivity::class.java)
                    println(country[position].weather_id)
                    intent.putExtra("country_code", country[position].weather_id)
                    startActivity(intent)
                }
            }
        }
        recyclerView.adapter = adapter

        loadProvince()

        return rootView
    }

}

