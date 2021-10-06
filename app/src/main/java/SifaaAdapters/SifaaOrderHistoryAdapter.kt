package SifaaAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import SifaaDataModels.SifaaOrderHistoryItem
import com.example.floral.R

class SifaaOrderHistoryAdapter(var context: Context, private var sifaaOrderHistoryList: ArrayList<SifaaOrderHistoryItem>) :
    RecyclerView.Adapter<SifaaOrderHistoryAdapter.ItemListViewHolder>() {

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTV = itemView.findViewById<TextView>(R.id.order_history_item_date_tv)
        val orderIDTV = itemView.findViewById<TextView>(R.id.order_history_item_order_id_tv)
        val orderStatusTV = itemView.findViewById<TextView>(R.id.order_history_item_order_status_tv)
        val orderPaymentTV = itemView.findViewById<TextView>(R.id.order_history_item_order_payment_tv)
        val priceTV = itemView.findViewById<TextView>(R.id.order_history_item_price_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sifaa_record_order_item, parent, false)
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {

        val currentItem = sifaaOrderHistoryList[position]

        holder.dateTV.text = currentItem.date
        holder.orderIDTV.text = currentItem.orderId
        holder.orderStatusTV.text = currentItem.orderStatus
        holder.orderPaymentTV.text = currentItem.orderPayment
        holder.priceTV.text = currentItem.price

    }

    override fun getItemCount(): Int = sifaaOrderHistoryList.size

}