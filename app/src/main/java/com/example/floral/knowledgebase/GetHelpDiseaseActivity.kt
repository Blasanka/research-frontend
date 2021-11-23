package com.example.floral.knowledgebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.floral.databinding.ActivityGetHelpDiseaseBinding
import com.example.floral.disease.Constants
import com.example.floral.disease.ResultResponse
import com.example.floral.knowledgebase.data.DiseaseDetails
import com.example.floral.knowledgebase.data.ClusterMap
import com.google.gson.GsonBuilder
import com.kaopiz.kprogresshud.KProgressHUD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetHelpDiseaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetHelpDiseaseBinding
    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetHelpDiseaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true);

        hud!!.show()

        val response = intent.extras?.get("result") as ResultResponse
        val result = response.result;
        if (result.identifiedDisease != null)
            binding.guidanceTv.text = result.toString()


        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val knowledgeableService = retrofit.create(KnowledgeableService::class.java)
        val body = result.identifiedDisease?.let { result.flowerName?.let { it1 -> DiseaseDetails(diseaseName= it, flowerName = it1) } }
        // Fetch the national data
        if (body != null) {
            knowledgeableService.getHelpForDisease(body).enqueue(object : Callback<ClusterMap> {
                override fun onResponse(call: Call<ClusterMap>, response: Response<ClusterMap>) {
                    Log.i(Constants.TAG, "onResponse: $response")
                    val data = response.body()
                    if (data == null) {
                        Log.w(Constants.TAG, "Did not received a valid response body")
                        return
                    }
                    Log.i(Constants.TAG, "Update result view with guidance data")
                    Log.i(Constants.TAG, data.toString())
                    hud?.dismiss()
                    binding.guidanceTv.text = data.toString()
                }

                override fun onFailure(call: Call<ClusterMap>, t: Throwable) {
                    Log.e(Constants.TAG, "onFailure: $t")
                    hud?.dismiss();
                }
            })
        }
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}