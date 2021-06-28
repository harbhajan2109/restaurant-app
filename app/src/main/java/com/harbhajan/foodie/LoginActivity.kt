package com.harbhajan.foodie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var etUserName: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var forgotPassword: TextView
    var userId:String?=""
    var pass:String?=""
    lateinit var sharedPreferences: SharedPreferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin = findViewById(R.id.btnLogin)
        forgotPassword = findViewById(R.id.txtForgotPassword)
        etUserName = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        sharedPreferences=getSharedPreferences(getString(R.string.logged_in), Context.MODE_PRIVATE)
        var isLoogedIn=sharedPreferences.getBoolean("isLoggedIn",false)
        if (isLoogedIn){
            val intent =
                Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val intent =
                Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)

            val username = etUserName.text.toString()
            val password = etPassword.text.toString()
            if (username.length <4) {
                Toast.makeText(this@LoginActivity, "incorrect username", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.length < 4) {
                Toast.makeText(this@LoginActivity, "min length 4 characters!!!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val queue = Volley.newRequestQueue(this@LoginActivity)
                val url ="http://techblr.xyz/admin/restaurant_login/"
                userId="?userid=$username"
                pass="&password=$password"
                val jsonParams=JSONObject()
                jsonParams.put("username",username)
                jsonParams.put("password",password)
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url + userId + pass, jsonParams, Response.Listener {
                        try {

                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            val response = data.getJSONObject("data")
                            setSharedPreferences(response)
                            if (success) {
                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()



                            }
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@LoginActivity,
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

            }


        }


        forgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)
        }
    }
    fun setSharedPreferences(response:JSONObject){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
        sharedPreferences.edit().putString("ResId",response.getString("id")).apply()
        sharedPreferences.edit().putString("RestaurantName",response.getString("restaurant_name")).apply()

    }
}
