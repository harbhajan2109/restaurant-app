package com.harbhajan.foodie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class OrdersFragment : Fragment() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:OrderRecyclerAdapter
    lateinit var recyclerOrder:RecyclerView
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    private  var orderDetails=ArrayList<Orders>()
    lateinit var sharedPreferences: SharedPreferences
    var urlId:String?=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerOrder = view.findViewById(R.id.recyclerOrder)
        sharedPreferences=context?.getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)!!
        /*  val orderList= arrayListOf<Orders>(
            Orders("OrderId: #21","brampton,toronto,canada","maharaja mac","http/31",
                "Mcdonelds","quantity=1.0","25","06/01/2021","04:02")
        )

       */
        val resId=sharedPreferences.getString("ResId","")
        layoutManager = LinearLayoutManager(activity)
        val queue = Volley.newRequestQueue(activity as Context)
        val url ="http://techblr.xyz/admin/orders_restaurant/"
        urlId="?rid=$resId"
        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url + urlId, null, Response.Listener {
                try {
                    progressLayout.visibility=View.GONE
                    progressBar.visibility=View.GONE
                    val data=it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val menuItem = data.getJSONObject(i)
                        val orderItems = Orders(
                            menuItem.getString("id"),
                            menuItem.getString("name"),
                            menuItem.getString("mobile"),
                            menuItem.getString("order_id"),
                            menuItem.getString("address"),
                            menuItem.getString("total_item"),
                            menuItem.getString("total_amount")
                        )

                        orderDetails.add(orderItems)

                        recyclerAdapter = OrderRecyclerAdapter(activity as Context, orderDetails)
                        recyclerOrder.adapter = this.recyclerAdapter
                        recyclerOrder.layoutManager = this.layoutManager
                    }


                } catch (e: JSONException) {
                    Toast.makeText(
                        activity as Context,
                        "Some Unexpected Error Occurred $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, Response.ErrorListener {
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
                    headers["Content-Type"]="application/json"
                    return headers
                    }
            }
        queue.add(jsonObjectRequest)

        return view
    }


}