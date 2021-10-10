package com.example.floral

import SifaaAdapters.SifaaFloralFavAdapter
import SifaaDataModels.SifaaFloralItem
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testfloral.RetrofitInstance
import interfaces.FloralApi
import interfaces.RequestType
import kotlinx.android.synthetic.main.activity_sifaa_previous.*
import retrofit2.HttpException
import java.io.IOException



class SifaaPrevious : AppCompatActivity(), FloralApi {

    private var zPlusAxes = ArrayList<Int>()
    @SuppressLint("StaticFieldLeak")
    private lateinit var sifaaFavAdapter: SifaaFloralFavAdapter
    private var allItems3 = ArrayList<SifaaFloralItem>()
    private lateinit var itemRecyclerView3: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sifaa_previous)

        items_recycler_view3.adapter = sifaaFavAdapter
        items_recycler_view3.layoutManager = LinearLayoutManager(this)

    }


    override fun onFetchSuccessListener(list: ArrayList<SifaaFloralItem>, requestType: RequestType) {

        lifecycleScope.launchWhenCreated {


            //  progressBar2.isVisible = true
            val response = try {
                RetrofitInstance.api.getFav()

            } catch (e: IOException) {
                Log.e("TAG", "IOException, you might not have internet connection")

                //     progressBar2.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("TAG", "HttpException, unexpected response")

                //     progressBar2.isVisible = false
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {

                //     progressBar2.isVisible = false
                Log.e(response.body()!![0].fav.toString(), "HttpException, unexpected response")
                if (requestType == RequestType.READ) {
                    for (item in list) {
                        for (i in response.body()!!.indices) {
                            zPlusAxes.add(response.body()!![i].fav)
                            if (item.itemID == zPlusAxes[i].toString()) {
                                allItems3.add(item)
                            }
                        }
                    }


                    sifaaFavAdapter.notifyItemRangeInserted(0, allItems3.size)
                }

                //  todoAdapter.todos = response.body()!!
                //Log.e(response.body()!![0].name, "HttpException, unexpected response")
                //response.body()!![0].name = recItemValidatedName


                Log.e(zPlusAxes[0].toString(), "expected response")

            } else {
                Log.e("hi", "HttpException, unexpectedasdasdasd response")
                //   Log.e(TAG, "Response not successful")
            }
            // binding.progressBar.isVisible = false


        }
    }

    fun goBack(view: View) {onBackPressed()}


        }

