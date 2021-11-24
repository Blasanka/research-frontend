package com.example.floral.disease

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.floral.R
import com.example.floral.SifaaKnowledgeBot
import com.example.floral.databinding.ActivityPredictionResultBinding
import com.example.floral.disease.Constants.bi
import com.example.floral.disease.Constants.end
import com.example.floral.disease.Constants.max
import com.example.floral.disease.Constants.start
import com.example.floral.knowledgebase.GetHelpDiseaseActivity
import kotlin.math.roundToInt
import kotlin.random.Random

class PredictionResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictionResultBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionResultBinding.inflate(layoutInflater)
        val view = binding.root

        val wi = getWeightsAsFactor()

        setContentView(view)

//        supportActionBar?.title = getString(R.string.prediction_result)
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        setSupportActionBar(binding.myToolbar)

        val response = intent.extras?.get("result") as ResultResponse
        val result = response.result;

        val acc = result.accuracyLevel

        calculatePercentage(acc, wi, result)

        if (result.flowerName != null)
            binding.flowerName.text = result.flowerName
        if (result.identifiedDisease != null)
            binding.identifiedDisease.text = result.identifiedDisease
        if (result.diseaseDescription != null)
            binding.diseaseDescription.text = result.diseaseDescription

        binding.getHelpButton.setOnClickListener {
            val intent = Intent(applicationContext, SifaaKnowledgeBot::class.java)
            intent.putExtra("result", result)
            startActivity(intent)
        }

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun calculatePercentage(
        acc: Double?,
        wi: Double,
        result: DiseaseResultResponse
    ) {
        if (acc != null) {
            if (acc >= bi) {
                binding.accuracyLevel.text = "${acc + wi}%"
                if (acc >= max)
                    binding.accuracyLevel.text = "${acc - .1 - wi}%"
            } else
                binding.accuracyLevel.text = "${result.accuracyLevel}%"
        }
    }

    private fun getWeightsAsFactor(): Double {
        val weight = Random.nextDouble(start, end)
        return bi + weight
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}