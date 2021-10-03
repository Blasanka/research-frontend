package com.example.floral.suggestSystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.floral.R
//import com.example.floral.suggestSystem.fragments.SaveFragment
//import com.example.floral.suggestSystem.fragments.UpdateFragment
import com.example.floral.suggestSystem.model.User
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var userList = emptyList<User>()
    private var position : Int = 0
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.imageView_custom)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
          val currentItem = userList[position]
//        holder.itemViewtvPostId.text = currentItem.postId.toString()
//        holder.itemView.tvIid.text = currentItem.iid
          holder.itemView.tvName.text = currentItem.name
          Glide.with(holder.itemView).load("${currentItem.postId}").into(holder.itemImage)
//        holder.itemView.customImg.setImageResource(currentItem.)
//        holder.itemView.tvEmail.text = currentItem.email.toString()
//        holder.itemView.tvBody.text = currentItem.body.toString()
          holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity
      //          val updateFragment = UpdateFragment()
      //          updateFragment.adapterUpdatePos(holder.adapterPosition,currentItem)
      //          activity.supportFragmentManager.beginTransaction()
       //             .replace(R.id.flFragment, updateFragment).commit()
                // saveFragment.etName.
            }
        })
    }



    fun adapterPosList(no: Int){
        position = no
    }


    fun setData(user: List<User>) {
            this.userList = user
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return userList.size
        }
    }







