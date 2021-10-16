package com.example.floral

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.floral.databinding.ActivityDiseaseMainBinding
import com.example.floral.disease.*

class DiseaseMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiseaseMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detectorCv.setOnClickListener {
            val intent = Intent(this, DiseaseDetectorActivity::class.java)
            startActivity(intent)
        }

        binding.allDiseaseCv.setOnClickListener {
            val intent = Intent(this, DiseaseListActivity::class.java)
            startActivity(intent)
        }

        binding.flowersCv.setOnClickListener {
            val intent = Intent(this, FlowerListActivity::class.java)
            startActivity(intent)
        }

        binding.insectsCv.setOnClickListener {
            val intent = Intent(this, InsectsListActivity::class.java)
            startActivity(intent)
        }

    }

    fun goBack(view: View) {
        onBackPressed()
    }

}