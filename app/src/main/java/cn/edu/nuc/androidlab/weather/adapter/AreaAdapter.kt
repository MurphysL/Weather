package cn.edu.nuc.androidlab.weather.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.edu.nuc.androidlab.weather.R
import java.util.*

/**
 * AreaAdapter
 * Created by MurphySL on 2017/7/17.
 */
class AreaAdapter(private val data : ArrayList<String>) : RecyclerView.Adapter<MyViewHolder>() {
    private val TAG : String = this.javaClass.simpleName

    var listener : MyClickListener? = null

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder?.textView?.text = data[position]
        holder?.itemView?.setOnClickListener{if(listener != null) listener!!.onClick(position)}
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val root = LayoutInflater.from(parent?.context).inflate(R.layout.item_area, parent, false)
        return MyViewHolder(root)
    }

    override fun getItemCount(): Int = data.size

}

class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    var textView : TextView = itemView.findViewById(R.id.textView)
}

interface MyClickListener {
    fun onClick(position : Int)
}