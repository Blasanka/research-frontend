package com.example.floral

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import kotlinx.android.synthetic.main.fragment_suggest.*

class SuggestFragment : Fragment(R.layout.fragment_suggest) {

    private var paramChannelList = ArrayList<String>()
    private var paramSwitchList = ArrayList<Boolean>()

    private lateinit var paraAdapter: ParamAdapter

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // setupRecyclerView()
       // to()
        //SugHorizontalRecyclerVIew
        val posts: ArrayList<String> = ArrayList()
        val bookList: ArrayList<String> = ArrayList()
        for(i in 1..100){
            posts.add("Flower $i")
        }
        for(i in 1..100){
            bookList.add("Plant $i")
        }
        recyclerView.layoutManager = LinearLayoutManager(context!!, OrientationHelper.HORIZONTAL,false)
        recyclerView.adapter = PostsAdapter(posts)
        books.layoutManager = LinearLayoutManager(context!!, OrientationHelper.HORIZONTAL,false)
        books.adapter = PostsAdapter(bookList)
        //Search
        val search = searchView
        val listView = listView
        val names = arrayOf("Android", "Kotlin", "Java", "C++", "C#")
        val adapter : ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_list_item_1,names)
        listView.adapter = adapter
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if(names.contains(query)){
                    adapter.filter.filter(query)
                }else{
                    Toast.makeText(context,"item not fount", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false

            }

        })


    }

    private fun to(){

        addToChannel("Test",true)
        addToChannel("Test2",true)
        addToChannel("Test3",true)



    }       // private fun setupRecyclerView() = rvChannels.apply {
          //  paraAdapter = ParamAdapter(paramChannelList,paramSwitchList)
         //   adapter = paraAdapter
          //  layoutManager = LinearLayoutManager(context)

        }

private fun <E> ArrayList<E>.add(element: Int) {

}


private fun addToChannel(titles: String , switch : Boolean){
        //    paramChannelList.add(titles)
        //    paramSwitchList.add(switch)
        }


