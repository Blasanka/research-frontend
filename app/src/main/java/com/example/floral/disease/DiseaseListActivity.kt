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
                "Powdery Mildew",
                "Fungus that produces airborne spores from infected stems or buds on roses. After overwintering on your plants, the disease is most likely to flare up if the roots are in dry soil and the leaves are in humid air – conditions that are often found when plants are grown near or against a wall."
            ),
            Disease("2",
                "default_sifaa_image",
                "Botrytis",
                "Also know as gray mold, is a fungal disease caused by several species in the genus Botrytis. It affects the buds, flowers, leaves, and bulbs of many plants including: African violet, begonia, chrysanthemum, cyclamen, dahlia, geranium, lily, peony, rose, and tulip."
            ),
            Disease("3",
                "default_sifaa_image",
                "Wilt",
                "Plants suddenly wilt after warm, dry periods in early summer. Upper leaves droop; in a few days all leaves droop, and the plant dies. Plants may also bend over or break."
            ),
            Disease("4",
                "Root Rot",
                "Botrytis",
                "When sunflower roots come in contact with the sclerotia, the sclerotia germinate and infect the roots. The fungus grows from the infected root into the taproot and forms a canker at the stem base."
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