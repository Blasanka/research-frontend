package com.example.floral.disease.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floral.R
import com.example.floral.disease.InsectsListActivity
import com.example.floral.disease.data.Insect
import kotlin.collections.ArrayList

class InsectsListAdapter(
    var context: Context,
    private var insects: ArrayList<Insect>,
    val listener: InsectsListActivity
) :
    RecyclerView.Adapter<InsectsListAdapter.ItemListViewHolder>() {

    private var insectsList = ArrayList<Insect>(insects)

    interface OnItemClickListener {
        fun onItemClick(insect: Insect)
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val insectIV: ImageView = itemView.findViewById(R.id.insectIv)
        val insectNameTV: TextView = itemView.findViewById(R.id.insectNameTv)
        val insectDesc: TextView = itemView.findViewById(R.id.insectCatTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_insect, parent, false)

        insectsList = ArrayList(insects)
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val insect = insects[position]

        holder.insectIV.setImageResource(R.drawable.default_sifaa_image)
        holder.insectNameTV.text = insect.name
        holder.insectDesc.text = insect.description

        holder.itemView.setOnClickListener {
            listener.onItemClick(insect)
        }
    }

    override fun getItemCount(): Int = insects.size
}