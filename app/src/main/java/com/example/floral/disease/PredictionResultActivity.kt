package com.example.floral.disease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.floral.R
import com.example.floral.databinding.ActivityPredictionResultBinding

class PredictionResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictionResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = getString(R.string.prediction_result)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val response = intent.extras?.get("result") as ResultResponse
        val result = response.result;
        if (result.flowerName != null)
            binding.flowerName.text = result.flowerName
        if (result.identifiedDisease != null)
            binding.identifiedDisease.text = result.identifiedDisease
        if (result.diseaseDescription != null)
            binding.diseaseDescription.text = result.diseaseDescription
        if (result.accuracyLevel != null)
            binding.accuracyLevel.text = "${result.accuracyLevel}%"

        binding.getHelpButton.setOnClickListener {
            finish()
        }

        binding.closeButton.setOnClickListener {
            finish()
        }
    }
}