package com.harbhajan.foodie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_contact_us_dialog_box.view.*
import kotlinx.android.synthetic.main.activity_status_restayrant_box.*
import org.json.JSONException
import org.json.JSONObject

class StatusDialogBox: DialogFragment() {
    lateinit var checkBoxOpen: CheckBox
    lateinit var checkBoxClose: CheckBox
    lateinit var btnUpdate:Button
    lateinit var sharedPreferences: SharedPreferences
    var statusP: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_status_restayrant_box, container, false)
        checkBoxOpen=view.findViewById(R.id.checkboxOpen)
        checkBoxClose=view.findViewById(R.id.checkboxClosed)
        btnUpdate=view.findViewById(R.id.btnUpdate)
        sharedPreferences=context?.getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)!!
        btnUpdate.setOnClickListener {
            if (statusP == null) {
                Toast.makeText(context, "Please check atleast one checkbox!!", Toast.LENGTH_SHORT)
                    .show()

            } else if (checkBoxOpen.isChecked && checkBoxClose.isChecked) {
                Toast.makeText(context, "Please select only one checkbox!!", Toast.LENGTH_SHORT).show()
            } else {
                when {
                        checkBoxOpen.isChecked -> {
                        statusP = "Open"
                        // holder.checkboxUnavailable.isEnabled = false
                        statusProduct(statusP!!)


                    }
                        checkBoxClose.isChecked -> {
                        statusP = "Closed"
                        // holder.checkBoxAvailable.isEnabled = false
                        statusProduct(statusP!!)

                    }
                }

            }


        }



        return view
    }
    private fun statusProduct(status:String){
        val resId=sharedPreferences.getString("ResId","")
        val queue = Volley.newRequestQueue(context)
        val url ="http://techblr.xyz/admin/restaurant_status/"
        val jsonParams= JSONObject()

        var uid="?rid=$resId"
        var ustatus="&status=$status"
        jsonParams.put("product_id",id)
        jsonParams.put("status",status)
        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url + uid + ustatus   , jsonParams, Response.Listener {
            try {
                val data = it.getJSONObject("data")
                val message = data.getString("Message")
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                val intent=Intent(activity as Context,MainActivity::class.java)
                startActivity(intent)
            }catch (e: JSONException){
                Toast.makeText(
                    context,
                    "Something Went Wrong!!$e",
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
    }

}