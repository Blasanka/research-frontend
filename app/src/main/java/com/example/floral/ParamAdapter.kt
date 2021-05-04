package com.example.floral

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.flo_item.view.*


class ParamAdapter(private var titles: ArrayList<String>,
                   private var switchs: ArrayList<Boolean>

): RecyclerView.Adapter<ParamAdapter.ParamViewHolder>() {


    //private var device: Device? = null
    //private var channel: Channel? = null

    inner class ParamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvChannelNew: TextView = itemView.findViewById(R.id.tvChannelNew)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flo_item,parent,false)
        return ParamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParamViewHolder, position: Int) {
        holder.itemView.apply {
            tvChannelNew.text = titles[position]
            swOn.isChecked = switchs[position].toString().toBoolean()
            //swOn.isChecked = param[position]
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}