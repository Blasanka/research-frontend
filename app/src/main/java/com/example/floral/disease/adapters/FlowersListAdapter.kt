package com.example.floral.disease.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floral.R
import com.example.floral.disease.FlowerListActivity
import com.example.floral.disease.data.Flower
import kotlin.collections.ArrayList

class FlowersListAdapter(
    var context: Context,
    private var flowers: ArrayList<Flower>,
    val listener: FlowerListActivity
) :
    RecyclerView.Adapter<FlowersListAdapter.ItemListViewHolder>() {

    private var flowersList = ArrayList<Flower>(flowers)

    interface OnItemClickListener {
        fun onItemClick(flower: Flower)
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flowerIV: ImageView = itemView.findViewById(R.id.flowerIv)
        val flowerNameTV: TextView = itemView.findViewById(R.id.flowerNameTv)
        val flowerDesc: TextView = itemView.findViewById(R.id.flowerCatTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_flower, parent, false)

        flowersList = ArrayList(flowers)
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val flower = flowers[position]

        holder.flowerIV.setImageResource(R.drawable.default_sifaa_image)
        holder.flowerNameTV.text = flower.name
        holder.flowerDesc.text = flower.description


        holder.itemView.setOnClickListener {
            listener.onItemClick(flower)
        }
    }

    override fun getItemCount(): Int = flowers.size
}