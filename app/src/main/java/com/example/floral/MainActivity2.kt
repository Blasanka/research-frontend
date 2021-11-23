package com.example.floral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.testfloral.RetrofitInstance
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_price_range.*
import retrofit2.HttpException
import java.io.IOException

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

    lifecycleScope.launchWhenCreated {
        val response = try{
            RetrofitInstance.api.getCluster()
        }catch (e: IOException){
            Log.e("Path", "IOException, you might not have internet connection")
            return@launchWhenCreated
        }
        catch (e: HttpException){
            Log.e("Path", "IOException, you might not have internet connection")
            return@launchWhenCreated
        }
        if(response.isSuccessful && response.body() != null){
            path.text = response.body()!!.results.toString()

            Log.e("Done",response.body()!!.results.toString())
//
//            price_range.text = response.body()!!.results.toString()
//            Log.e("Done",response.body()!!.results.toString())
        }


    }

    }
}