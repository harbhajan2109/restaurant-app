package com.harbhajan.foodie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    lateinit var etMobileNumber:EditText
    lateinit var etEnterOtp:EditText
    lateinit var btnSubmit1:Button
    lateinit var btnSubmit2:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etMobileNumber=findViewById(R.id.etMobileNumber)
        etEnterOtp=findViewById(R.id.etOtp)
        btnSubmit1=findViewById(R.id.btnSubmit1)
        btnSubmit2=findViewById(R.id.btnSubmit2)
        btnSubmit1.setOnClickListener {
            var mobile=etMobileNumber.text.toString()
            val queue = Volley.newRequestQueue(this@ForgotPassword)
            val url="http://techblr.xyz/admin/update-password/"
            val jsonParams=JSONObject()
            jsonParams.put("mobile_number","mobile")
            val jsonRequest=object : JsonObjectRequest(Request.Method.POST,url,jsonParams,Response.Listener {
                try {
                    val data=it.getJSONObject("data")
                    val message=it.getJSONObject("message")
                }catch (e:JSONException){

                }
            },Response.ErrorListener{

            })
            {

            }
        }
    }
}