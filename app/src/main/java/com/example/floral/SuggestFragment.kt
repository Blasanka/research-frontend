package com.example.floral

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.viewpager2.widget.ViewPager2
import com.example.floral.suggestSystem.adapters.TodoAdapter
import com.example.floral.suggestSystem.adapters.ViewPagerAdapter
import com.example.floral.suggestSystem.retrofit.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_suggest.*
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val TAG = "MainActivity"
val CITY: String = "colombo,western"
val API: String = "c0708ff64a3ccc83b55761b4daa1e033" // Use API key

class SuggestFragment : Fragment(R.layout.fragment_suggest) {
    private var paramChannelList = ArrayList<String>()
    private var paramSwitchList = ArrayList<Boolean>()
    private lateinit var paraAdapter: ParamAdapter
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var viewModel: MainViewModel
//    private var response : <Todo>
    private val todoAdapter by lazy { TodoAdapter() }
    private var titles = ArrayList<String>()
    private var details: String? = null
    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//             setupRecyclerView()
//             to()
//             private var myList = ArrayList<Todo>()
//             binding = ActivityMainBinding.inflate(layoutInflater)
//             setContentView(binding.root)
//             weatherTask().execute()
               weatherTask().execute()
            var images = listOf(
                R.drawable.sug_slider_1,
                R.drawable.sug_slider_2,
                // R.drawable.sample7
            )
            val adapter = ViewPagerAdapter(images)
            viewPager.adapter = adapter
            viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
            viewPager.beginFakeDrag()
            viewPager.fakeDragBy(-10f)
            viewPager.endFakeDrag()
            rvTodos.adapter = todoAdapter
            rvTodos.layoutManager = LinearLayoutManager(context!!, OrientationHelper.HORIZONTAL, false)
//            rvTodos2.adapter = todoAdapter
//            rvTodos2.layoutManager = LinearLayoutManager(context!!, OrientationHelper.HORIZONTAL, false)
            lifecycleScope.launchWhenCreated {
                progressBar2.isVisible = true
                val response = try {
                    RetrofitInstance.api.getTodos()
                } catch (e: IOException) {
                    RecSnackBar()
                    Log.e(TAG, "IOException, you might not have internet connection")
                    progressBar2.isVisible = false
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    RecSnackBar()
                    Log.e(TAG, "HttpException, unexpected response")
                    progressBar2.isVisible = false
                    return@launchWhenCreated
                }
                if (response.isSuccessful && response.body() != null) {
                    //  response.body()!!.data[0].name.let { todoAdapter.setData(it) }
                    val paramLength = response.body()!!.data.size - 1
//                    val pose: ArrayList<ImageView> = ArrayList()
                    for (i in 0..paramLength) {
                        todoAdapter.setData(
                            response.body()!!.data[i].name,
                            response.body()!!.data[i].price,
                            response.body()!!.data[i].product_id
                        )
                    }
//                textView.text = response.body()!!.data[0].name

                } else {
                    RecSnackBar()
                    Log.e(TAG, "Response not successful")
                }
                progressBar2.isVisible = false
            }
    }
//        val repository = Repository()
//        val viewModelFactory = MainViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
//        viewModel.getTodos()
//        viewModel.myCustomPosts.observe(this, { response ->
//            if(response.isSuccessful){
//                response.body()!!.data[0].name.let { todoAdapter.setData(it) }
//            }else {
//                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//            }
//        })
//  postToList(response.body()!!.data[0].name)
        private fun RecSnackBar() {
            Snackbar.make(
                suggestMain, "you might not have internet connection", Snackbar.LENGTH_INDEFINITE,
            ).setAction(
                "retry"
            ) {
                suggestMain.setBackgroundColor(Color.parseColor("#f2f2f2"))
            }.show()
        }
        @SuppressLint("StaticFieldLeak")
        inner class weatherTask() : AsyncTask<String, Void, String>() {
            override fun onPreExecute() {
                super.onPreExecute()
                /* Showing the ProgressBar, Making the main design GONE */
                //  findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
                mainContainer.visibility = View.GONE
                ///  findViewById<TextView>(R.id.errorText).visibility = View.GONE
            }
            override fun doInBackground(vararg params: String?): String? {
                var response:String?
                try{
                    response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
                }catch (e: Exception){
                    response = null
                }
                return response
            }
            @SuppressLint("SetTextI18n")
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                try {
                    /* Extracting JSON returns from the API */
                    val jsonObj = JSONObject(result)
                    val main = jsonObj.getJSONObject("main")
                    val sys = jsonObj.getJSONObject("sys")
                    val wind = jsonObj.getJSONObject("wind")
                    val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                    val updatedAt: Long = jsonObj.getLong("dt")
                        "Updated at: " + SimpleDateFormat(
                            "dd/MM/yyyy hh:mm a",
                            Locale.ENGLISH
                        ).format(
                            Date(updatedAt * 1000)
                        )
                    val temp = main.getString("temp") + "°C"
//                    val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
//                    val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                    val pressure = main.getString("pressure")
                    val humidity = main.getString("humidity")
                    val sunrise: Long = sys.getLong("sunrise")
                    val sunset: Long = sys.getLong("sunset")
                    val windSpeed = wind.getString("speed")
                    val weatherDescription = weather.getString("description")
                    val address = jsonObj.getString("name") + ", " + sys.getString("country")
                    city.text = "Recommended Products In $address"
                    statuss.text = weatherDescription.capitalize(Locale.ROOT)
                    about.text = temp
                    sunrises.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                    sunsets.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                    winds.text = windSpeed
                    pressures.text = pressure
                    humiditys.text = humidity
                } catch (e: Exception) {
//                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
//                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                }

            }
        }
        private fun addToList(title: String) {
            titles.add(title)
        }
        private fun postToList(detail: String) {

            addToList(detail)
        }
    }
