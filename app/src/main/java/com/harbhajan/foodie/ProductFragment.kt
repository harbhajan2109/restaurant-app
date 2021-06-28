package com.harbhajan.foodie

import android.content.Context
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

class ProductFragment : Fragment() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:ProductRecyclerAdapter
    lateinit var recyclerProduct: RecyclerView
    lateinit var progressLayout: RelativeLayout
    private  var productDetails=ArrayList<Products>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_product, container, false)
        progressLayout=view.findViewById(R.id.progressLayout)
        recyclerProduct=view.findViewById(R.id.recyclerProduct)
        // Inflate the layout for this fragment
      /*  var productList= arrayListOf<Products>(
            Products("OnePlus","SmartPhone","500","http/31")
        )

       */

        layoutManager= LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)
        val url ="http://www.techblr.xyz/admin/resturant-orderedProducts/"
        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                try {
                    progressLayout.visibility=View.GONE
                    val data=it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val menuItem = data.getJSONObject(i)
                        val productItems = Products(
                            menuItem.getString("product_id"),
                            menuItem.getString("product"),
                            menuItem.getString("category"),
                            menuItem.getString("image"),
                            menuItem.getString("total_price")
                        )

                        productDetails.add(productItems)

                        recyclerAdapter= ProductRecyclerAdapter(activity as Context,productDetails)
                        recyclerProduct.adapter=this.recyclerAdapter
                        recyclerProduct.layoutManager=this.layoutManager
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