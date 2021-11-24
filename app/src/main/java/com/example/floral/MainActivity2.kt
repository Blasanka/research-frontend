package com.example.floral

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.example.floral.disease.Constants
import com.example.floral.disease.ResultResponse
import com.example.floral.knowledgebase.GetHelpDiseaseActivity
import com.example.floral.knowledgebase.KnowledgeableService
import com.example.floral.knowledgebase.data.ClusterMap
import com.example.floral.knowledgebase.data.DiseaseDetails
import com.example.floral.location.LocationService
import com.example.testfloral.RetrofitInstance
import com.google.gson.GsonBuilder
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_price_range.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity2 : AppCompatActivity() {

    private lateinit var latEt: EditText
    private lateinit var longEt: EditText
    private lateinit var checkBt: Button

    private lateinit var mapIv: WebView
    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        latEt = findViewById(R.id.latEt)
        longEt = findViewById(R.id.longEt)
        checkBt = findViewById(R.id.checkId)

        val latitude = latEt.text.toString().toDoubleOrNull()
        val longitude = longEt.text.toString().toDoubleOrNull()

        mapIv = findViewById(R.id.mapIv)

        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true);

        hud!!.show()

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val locationService = retrofit.create(LocationService::class.java)
        requstMapAndPlotResult(locationService, latitude, longitude)


        checkBt.setOnClickListener {
            hud!!.show()
            requstMapAndPlotResult(locationService, latitude, longitude)
        }
//    lifecycleScope.launchWhenCreated {
//        val response = try{
//            RetrofitInstance.api.getCluster()
//        }catch (e: IOException){
//            Log.e("Path", "IOException, you might not have internet connection")
//            return@launchWhenCreated
//        }
//        catch (e: HttpException){
//            Log.e("Path", "IOException, you might not have internet connection")
//            return@launchWhenCreated
//        }
//        if(response.isSuccessful && response.body() != null){
//            path.text = response.body()!!.results.toString()
//
//            Log.e("Done",response.body()!!.results.toString())
////
////            price_range.text = response.body()!!.results.toString()
////            Log.e("Done",response.body()!!.results.toString())
//        }
//
//
//    }

    }

    private fun requstMapAndPlotResult(
        locationService: LocationService,
        latitude: Double?,
        longitude: Double?
    ) {
        locationService.getClusterMap(latitude ?: 6.5, longitude ?: 79.86)
            .enqueue(object : Callback<ClusterMap> {
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
    //                mapIv?.setImageURI(Uri.parse(data.result))

                    mapIv.settings.setJavaScriptEnabled(true)

                    mapIv.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            view?.loadUrl(url)
                            return true
                        }
                    }
                    hud?.dismiss()
                    mapIv.loadUrl(data.results)
                }

                override fun onFailure(call: Call<ClusterMap>, t: Throwable) {
                    Log.e(Constants.TAG, "onFailure: $t")
                    hud?.dismiss();
                }
            })
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}