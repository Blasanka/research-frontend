package com.example.floral.disease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floral.R
import com.example.floral.disease.adapters.InsectsListAdapter
import com.example.floral.disease.data.Insect
import com.example.floral.xAxes

class InsectsListActivity : AppCompatActivity(), InsectsListAdapter.OnItemClickListener {

    private lateinit var insectListAdapter: InsectsListAdapter
    private lateinit var insectsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insects_list)

        val insects = arrayListOf(
            Insect("1",
                "default_sifaa_image",
                getString(R.string.insect_thrips),
                getString(R.string.insect_info)
            )
        );

        insectListAdapter = InsectsListAdapter(
            applicationContext,
            insects,
            this
        )

        insectsRecyclerView = findViewById(R.id.insectsRv)

        insectsRecyclerView.adapter = insectListAdapter
        insectsRecyclerView.layoutManager = LinearLayoutManager(this@InsectsListActivity)

        insects.add(insects[0])
        insectListAdapter.notifyItemRangeInserted(xAxes.size, insects.size)
    }

    override fun onItemClick(insect: Insect) {
        TODO("Not yet implemented")
    }
    
    fun goBack(view: View) {
        onBackPressed()
    }
}