package com.example.floral.disease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floral.R
import com.example.floral.disease.adapters.DiseasesListAdapter
import com.example.floral.disease.data.Disease
import com.example.floral.xAxes

class DiseaseListActivity : AppCompatActivity(), DiseasesListAdapter.OnItemClickListener {

    private lateinit var diseaseListAdapter: DiseasesListAdapter
    private lateinit var diseasesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease_list)

        val diseases = arrayListOf(
            Disease("1",
                "default_sifaa_image",
                getString(R.string.disease_mildew),
                getString(R.string.disease_info)
            )
        );

        diseaseListAdapter = DiseasesListAdapter(
            applicationContext,
            diseases,
            this
        )

        diseasesRecyclerView = findViewById(R.id.diseasesRv)

        diseasesRecyclerView.adapter = diseaseListAdapter
        diseasesRecyclerView.layoutManager = LinearLayoutManager(this@DiseaseListActivity)

        diseases.add(diseases[0])
        diseaseListAdapter.notifyItemRangeInserted(xAxes.size, diseases.size)
    }

    override fun onItemClick(disease: Disease) {
        TODO("Not yet implemented")
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}