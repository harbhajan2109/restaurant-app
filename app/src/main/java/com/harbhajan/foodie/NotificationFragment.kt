package com.harbhajan.foodie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class NotificationFragment : Fragment() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:RecyclerNotificationAdapter
    lateinit var recyclerNotification: RecyclerView
    lateinit var progressLayout: RelativeLayout
    private  var notificationDetails=ArrayList<Notification>()
    lateinit var sharedPreferences: SharedPreferences
    var urlId:String?=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_notification, container, false)
        progressLayout=view.findViewById(R.id.progressLayout)
        recyclerNotification=view.findViewById(R.id.recyclerNotification)
        sharedPreferences=context?.getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)!!

        val resId=sharedPreferences.getString("ResId","")

        layoutManager= LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)
        val url ="http://techblr.xyz/admin/restaurant_assigned/"
        urlId="?rid=$resId"
        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url +urlId, null, Response.Listener {
                try {
                    progressLayout.visibility=View.GONE
                    val data=it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val menuItem = data.getJSONObject(i)
                        val notificationItems = Notification(
                            menuItem.getString("id"),
                            menuItem.getString("name"),
                            menuItem.getString("mobile"),
                            menuItem.getString("product"),
                            menuItem.getString("restaurant_name"),
                            menuItem.getString("time"),
                            menuItem.getString("quantity"),
                            menuItem.getString("image"),
                            menuItem.getString("order_id"),
                            menuItem.getString("address"),
                            menuItem.getString("total_amount")
                        )

                        notificationDetails.add(notificationItems)

                        recyclerAdapter=RecyclerNotificationAdapter(activity as Context,notificationDetails)
                        recyclerNotification.adapter=recyclerAdapter
                        recyclerNotification.layoutManager=layoutManager
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