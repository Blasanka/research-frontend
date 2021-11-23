package com.example.floral

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.example.floral.disease.Constants
import com.example.floral.disease.ResultResponse
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

    private var mapIv: ImageView? = null
    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

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
        locationService.getClusterMap().enqueue(object : Callback<ClusterMap> {
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
                mapIv?.setImageURI(Uri.parse(data.result))
            }

            override fun onFailure(call: Call<ClusterMap>, t: Throwable) {
                Log.e(Constants.TAG, "onFailure: $t")
                hud?.dismiss();
            }
        })

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

    fun goBack(view: View) {
        onBackPressed()
    }
}