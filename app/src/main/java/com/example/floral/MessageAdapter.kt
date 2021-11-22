package com.example.floral

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(var context:Context, var messageList:ArrayList<MessageClass>):RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.message_list_item, parent,false)
        return MessageViewHolder(view)

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        if(message.sender == 0) {
            val v = getUserLayout()
            holder.linearLayout.addView(v)
            val message_tv = v?.findViewById<TextView>(R.id.message_tv_user)
            message_tv?.setText(message.message)
        }else if(message.sender==1){
           val v = getBotLayout()
           holder.linearLayout.addView(v)
            val message_tv = v?.findViewById<TextView>(R.id.message_tv_bot)
            message_tv?.setText(message.message)
        }
    }

    override fun getItemCount(): Int {
        return  messageList.size
    }

    fun getBotLayout():FrameLayout?{
        val inflater: LayoutInflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.bot_message_box,null) as FrameLayout?
    }

    fun getUserLayout():FrameLayout?{
        val inflater: LayoutInflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.user_message_box,null) as FrameLayout?
    }

    class MessageViewHolder(view:View):RecyclerView.ViewHolder(view){
        val linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout)

    }
}