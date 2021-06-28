package com.harbhajan.foodie

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class ProductRecyclerAdapter(val context: Context, val productList: ArrayList<Products>): RecyclerView.Adapter<ProductRecyclerAdapter.HomeViewHolder>() {
    var id:String?=""
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_product_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val commonUrl = "http://www.techblr.xyz/admin/img/"
        val products = productList[position]
        var statusP: String? = ""
        id = products.productId
        holder.productId.text = products.productId
        holder.productName.text = products.productName
        holder.productCategory.text = products.productCategory
        holder.productPrice.text = products.productPrice
        Picasso.get().load(commonUrl + products.productImageUrl).error(R.drawable.oneplus)
            .into(holder.productImage)
        holder.btnUpdate.setOnClickListener {
            if (statusP == null) {
                Toast.makeText(context, "Please check atleast one checkbox!!", Toast.LENGTH_SHORT)
                    .show()

            } else if (holder.checkboxUnavailable.isChecked && holder.checkBoxAvailable.isChecked) {
                Toast.makeText(context, "Please select only one checkbox!!", Toast.LENGTH_SHORT).show()
            } else {
                when {
                    holder.checkBoxAvailable.isChecked -> {
                        statusP = "Available"
                       // holder.checkboxUnavailable.isEnabled = false
                        statusProduct(statusP!!)


                    }
                    holder.checkboxUnavailable.isChecked -> {
                        statusP = "Not Available"
                       // holder.checkBoxAvailable.isEnabled = false
                        statusProduct(statusP!!)

                    }
                }

            }


        }
    }
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName:TextView=view.findViewById(R.id.txtProductName)
        val productCategory:TextView=view.findViewById(R.id.txtProductCategory)
        val productPrice:TextView=view.findViewById(R.id.txtProductPrice)
        val checkBoxAvailable:CheckBox=view.findViewById(R.id.checkboxAvailable)
        val checkboxUnavailable:CheckBox=view.findViewById(R.id.checkboxUnavailable)
        val productImage:ImageView=view.findViewById(R.id.imgProduct)
        val productId:TextView=view.findViewById(R.id.txtProductId)
        val btnUpdate:Button=view.findViewById(R.id.btnUpdate)
    }
    fun statusProduct(status:String){

        val queue = Volley.newRequestQueue(context)
        val url ="http://techblr.xyz/admin/resturant-orderedProducts/"
        val jsonParams=JSONObject()

        var uid="?id=$id"
        var ustatus="&status=$status"
        jsonParams.put("product_id",id)
        jsonParams.put("status",status)
        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url + uid + ustatus   , jsonParams, Response.Listener {
            try {
                val data = it.getJSONObject("data")
                val message = data.getString("Message")
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }catch (e:JSONException){
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
