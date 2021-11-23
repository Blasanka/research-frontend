package com.example.floral.disease.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floral.R
import com.example.floral.disease.DiseaseListActivity
import com.example.floral.disease.data.Disease
import kotlin.collections.ArrayList

class DiseasesListAdapter(
    var context: Context,
    private var diseases: ArrayList<Disease>,
    val listener: DiseaseListActivity
) :
    RecyclerView.Adapter<DiseasesListAdapter.ItemListViewHolder>() {

    private var diseasesList = ArrayList<Disease>(diseases)

    interface OnItemClickListener {
        fun onItemClick(disease: Disease)
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val diseaseIV: ImageView = itemView.findViewById(R.id.diseaseIv)
        val diseaseNameTV: TextView = itemView.findViewById(R.id.diseaseNameTv)
        val diseaseDesc: TextView = itemView.findViewById(R.id.diseaseCatTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_disease, parent, false)

        diseasesList = ArrayList(diseases)
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val disease = diseases[position]

        holder.diseaseIV.setImageResource(R.drawable.default_sifaa_image)
        holder.diseaseNameTV.text = disease.name
        holder.diseaseDesc.text = disease.description

        holder.itemView.setOnClickListener {
            listener.onItemClick(disease)
        }
    }

    override fun getItemCount(): Int = diseases.size
}