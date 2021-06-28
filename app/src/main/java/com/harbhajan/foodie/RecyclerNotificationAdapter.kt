package com.harbhajan.foodie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class RecyclerNotificationAdapter(val context: Context, val notificationList: ArrayList<Notification>): RecyclerView.Adapter<RecyclerNotificationAdapter.HomeViewHolder>() {

    private val limit=5
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.activity_recycler_notification_adapter,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(notificationList.size>limit){
            limit
        }else{
            notificationList.size
        }

    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val commonUrl="http://www.techblr.xyz/admin/img/"
        sharedPreferences=context.getSharedPreferences("Log In",Context.MODE_PRIVATE)
        val order=notificationList[position]
        val items:String=order.quantity
        holder.id.text=order.id
        holder.productName.text=order.product
        holder.restaurantName.text=order.restaurant_name
        holder.time.text=order.time
        holder.quantity.text="Quantity:$items"
        holder.orderId.text=order.order_id
        holder.address.text=order.address
        holder.bill.text=order.total_amount
        holder.customerName.text=order.name
        holder.customerMobile.text=order.mobile

        //holder.time.text=order.time
        Picasso.get().load(commonUrl+order.image).error(R.drawable.black_panther).into(holder.productImage)

        /*holder.etTimeD.addTextChangedListener {
            holder.send.isEnabled =!(it.isNullOrEmpty())
        }

         */
        holder.send.setOnClickListener {
            val wTime = holder.etTimeD.text.toString()
            if (wTime.isNotEmpty()) {
                val id = order.id
                val rid = sharedPreferences.getString("ResId", "")
                val queue = Volley.newRequestQueue(context)
                val url = "http://techblr.xyz/admin/restaurant_time/"
                val jsonParams = JSONObject()

                val urid = "?rid=$rid"
                val uid = "&id=$id"
                val utime = "&r_time=$wTime"

                jsonParams.put("rid", rid)
                jsonParams.put("id", id)
                jsonParams.put("r_time", wTime)
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST,
                    url + urid + uid + utime,
                    jsonParams,
                    Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val message = data.getString("Message")
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            holder.waitTime.text = "Waiting time for delivery boy is: $wTime"
                            holder.etTimeD.visibility = View.GONE
                            holder.send.visibility = View.GONE
                            
                        } catch (e: JSONException) {
                            Toast.makeText(
                                context,
                                "Something Went Wrong!!$e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    },
                    Response.ErrorListener {
                        /*if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                     */
                        println("Error is $it")

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            }else{
                Toast.makeText(context,"Please enter time!!!",Toast.LENGTH_SHORT).show()
            }

        }



    }



    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.txtId)
        val orderId: TextView = view.findViewById(R.id.txtOrderId)
        val productName: TextView = view.findViewById(R.id.txtProductName)
        val restaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val address: TextView = view.findViewById(R.id.txtCustomerAddress)
        val bill: TextView = view.findViewById(R.id.txtBill)
        val time: TextView = view.findViewById(R.id.txtTime)
        val productImage: ImageView = view.findViewById(R.id.imgProductImage)
        val quantity: TextView = view.findViewById(R.id.txtQuantity)
        val customerName: TextView = view.findViewById(R.id.txtCustomerName)
        val customerMobile: TextView = view.findViewById(R.id.txtCustomerPhone)
        val waitTime: TextView = view.findViewById(R.id.txtWaitingTime)
        val etTimeD: EditText = view.findViewById(R.id.etTime)
        val send: Button = view.findViewById(R.id.btnSend)


    }


}