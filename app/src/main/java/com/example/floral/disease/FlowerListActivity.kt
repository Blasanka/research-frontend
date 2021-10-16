package com.example.floral.disease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.floral.R
import com.example.floral.disease.adapters.FlowersListAdapter
import com.example.floral.disease.data.Flower
import com.example.floral.xAxes

class FlowerListActivity : AppCompatActivity(), FlowersListAdapter.OnItemClickListener {

    private lateinit var flowerListAdapter: FlowersListAdapter
    private lateinit var flowersRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flower_list)

        val flowers = arrayListOf(
            Flower("1",
                "default_sifaa_image",
                getString(R.string.flower_rose),
                getString(R.string.flower_info)
            )
        );

        flowerListAdapter = FlowersListAdapter(
            applicationContext,
            flowers,
            this
        )

        flowersRecyclerView = findViewById(R.id.flowersRv)

        flowersRecyclerView.adapter = flowerListAdapter
        flowersRecyclerView.layoutManager = LinearLayoutManager(this@FlowerListActivity)

        flowers.add(flowers[0])
        flowerListAdapter.notifyItemRangeInserted(xAxes.size, flowers.size)
    }

    override fun onItemClick(flower: Flower) {
        TODO("Not yet implemented")
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}