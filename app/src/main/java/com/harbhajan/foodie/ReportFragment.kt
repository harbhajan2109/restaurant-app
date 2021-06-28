package com.harbhajan.foodie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_report.*
import org.json.JSONException
import org.json.JSONObject

class ReportFragment : Fragment() {
    lateinit var txtReport:EditText
    lateinit var btnCancel:Button
    lateinit var btnSubmit:Button
    var id:String?=""
    var feedback:String?=""

    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_report, container, false)
        txtReport=view.findViewById(R.id.etReport)
        btnCancel=view.findViewById(R.id.btnCancel)
        btnSubmit=view.findViewById(R.id.btnSubmit)
        sharedPreferences=context?.getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)!!
        id=sharedPreferences.getString("ResId","")
        feedback=txtReport.text.toString()
        txtReport.addTextChangedListener {
            btnSubmit.isEnabled =!(it.isNullOrEmpty())
        }
        btnSubmit.setOnClickListener {


                val queue = Volley.newRequestQueue(context)
                val url ="http://techblr.xyz/admin/restaurant_feedback/"
                val jsonParams= JSONObject()

                val uid="?rid=$id"
                var ustatus="&feedback=$feedback"
                jsonParams.put("rid",id)
                jsonParams.put("feedback",feedback)
                val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url + uid + ustatus   , jsonParams, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val message = data.getString("Message")
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
            val intent=Intent(activity as Context,MainActivity::class.java)
            startActivity(intent)
        }
        btnCancel.setOnClickListener {
            val intent=Intent(activity as Context,MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}