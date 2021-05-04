package com.example.floral

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_suggest.*

class SuggestFragment : Fragment(R.layout.fragment_suggest) {

    private var paramChannelList = ArrayList<String>()
    private var paramSwitchList = ArrayList<Boolean>()

    private lateinit var paraAdapter: ParamAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        to()


    }

    private fun to(){

        addToChannel("Test",true)
        addToChannel("Test2",true)
        addToChannel("Test3",true)



    }        private fun setupRecyclerView() = rvChannels.apply {
            paraAdapter = ParamAdapter(paramChannelList,paramSwitchList)
            adapter = paraAdapter
            layoutManager = LinearLayoutManager(context)

        }
        private fun addToChannel(titles: String , switch : Boolean){
            paramChannelList.add(titles)
            paramSwitchList.add(switch)
        }


}