package com.harbhajan.foodie

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class OrderRecyclerAdapter(val context: Context,val orderList: ArrayList<Orders>):RecyclerView.Adapter<OrderRecyclerAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val commonUrl="http://www.techblr.xyz/admin/img/"
        val order=orderList[position]
        holder.orderId.text=order.order_id
        holder.address.text=order.address
        holder.bill.text=order.total_amount
        holder.customerName.text=order.name
        holder.oid.text=order.id
        holder.customerMobile.text=order.mobile
        holder.items.text=order.total_items


        //holder.time.text=order.time

        holder.arrow.setOnClickListener {
            val intent=Intent(context,OrderHistoryActivity::class.java)
            intent.putExtra("Oid",order.id)
            intent.putExtra("OrderId",order.order_id)
            intent.putExtra("Address",order.address)
            intent.putExtra("Bill",order.total_amount)
            intent.putExtra("Name",order.name)
            intent.putExtra("Mobile",order.mobile)
            intent.putExtra("items",order.total_items)


            context.startActivity(intent)
        }
    }



    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId:TextView=view.findViewById(R.id.txtOrderId)
        val arrow:ImageView=view.findViewById(R.id.imgArrow)
        val address:TextView=view.findViewById(R.id.txtCustomerAddress)
        val bill:TextView=view.findViewById(R.id.txtBill)
        val customerName:TextView=view.findViewById(R.id.txtCustomerName)
        val customerMobile:TextView=view.findViewById(R.id.txtCustomerMobile)
        val oid:TextView=view.findViewById(R.id.txtOid)
        val items:TextView=view.findViewById(R.id.txtItems)

    }
}