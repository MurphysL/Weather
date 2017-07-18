package cn.edu.nuc.androidlab.weather.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.edu.nuc.androidlab.weather.R
import cn.edu.nuc.androidlab.weather.bean.Weather

/**
 * ForecastAdapter
 * Created by MurphySL on 2017/7/18.
 */
class ForecastAdapter(private val context : Context, private val forecast : List<Weather.HeWeather.DailyForecast>) : RecyclerView.Adapter<ForcastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ForcastViewHolder
            = ForcastViewHolder(LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false))


    override fun onBindViewHolder(holder: ForcastViewHolder?, position: Int) {
        with(forecast[position]){
            holder?.date?.text = date
            holder?.type?.text = cond.txt_d
            holder?.min_degree?.text = tmp.min
            holder?.max_degree?.text = tmp.max
        }
    }

    override fun getItemCount(): Int = forecast.size

}

class ForcastViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    val date : TextView = itemView.findViewById(R.id.date)
    val min_degree : TextView = itemView.findViewById(R.id.min_degree)
    val type : TextView = itemView.findViewById(R.id.type)
    val max_degree : TextView = itemView.findViewById(R.id.max_degree)
}