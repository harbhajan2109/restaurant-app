package com.harbhajan.foodie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class MoneyFragment : DialogFragment() {
    lateinit var txtTodayFund:TextView
    lateinit var txtTotalFund:TextView
    lateinit var sharedPreferences: SharedPreferences
    var urlId:String?=""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_money, container, false)
        txtTodayFund=view.findViewById(R.id.txtTodayFund)
        txtTotalFund=view.findViewById(R.id.txtTotalFund)
        sharedPreferences=context?.getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)!!
        val resId=sharedPreferences.getString("ResId","")
        val queue = Volley.newRequestQueue(activity as Context)
        val url ="http://techblr.xyz/admin/sales_details/"
        urlId="?rid=$resId"
        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url + urlId, null, Response.Listener {
                try {
                    val data=it.getJSONObject("data")
                    val totalFund=data.getString("total_amount")
                    val todayFund=data.getString("today_amount")
                    txtTotalFund.text=totalFund
                    txtTodayFund.text=todayFund


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