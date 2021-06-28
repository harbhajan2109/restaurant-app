package com.harbhajan.foodie

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.security.AllPermission

class OrderHistoryActivity : AppCompatActivity() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:AllProductsAdapter
    lateinit var recyclerOrder: RecyclerView
    private  var orderDetails=ArrayList<AllProducts>()
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    var orderId:String?=""
    var oid:String?=""


    var address:String?=""
    var bill:String?=""
    var totalItems:String?=""
    var name:String?=""
    var mobile:String?=""

    lateinit var txtOrderId:TextView
    lateinit var txtTime:TextView
    lateinit var txtItemNO:TextView
    lateinit var txtAddress:TextView
    lateinit var txtMoney:TextView

    lateinit var txtCustomerName:TextView
    lateinit var txtCustomerMobile:TextView
    lateinit var txtSubTotal:TextView
    lateinit var txtTax:TextView
    lateinit var txtTotal:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        if(intent!=null) {
            orderId = intent.getStringExtra("OrderId")

            address = intent.getStringExtra("Address")
            bill = intent.getStringExtra("Bill")
            totalItems=intent.getStringExtra("items")
            name=intent.getStringExtra("Name")
            mobile=intent.getStringExtra("Mobile")
            oid=intent.getStringExtra("Oid")
        }else{
            Toast.makeText(this@OrderHistoryActivity,"Something went wrong!1",Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
        recyclerOrder =findViewById(R.id.recyclerOrder)
        toolbar=findViewById(R.id.toolbar)
        txtOrderId=findViewById(R.id.txtOrderId)
        txtTime=findViewById(R.id.txtTime)
        txtItemNO=findViewById(R.id.txtItemsNo)
        txtAddress=findViewById(R.id.txtCustomerAddress)

        txtMoney=findViewById(R.id.txtMoney)

        txtCustomerName=findViewById(R.id.txtCustomerName)
        txtCustomerMobile=findViewById(R.id.txtCustomerPhone)
        txtSubTotal=findViewById(R.id.txtSubTotalRupee)
        txtTax=findViewById(R.id.txtTaxRupee)
        txtTotal=findViewById(R.id.txtTotalRupee)
        toolbar.title="Order Details"
        layoutManager = LinearLayoutManager(this)
        oid?.let { allProducts(it) }


        var t=bill?.toInt()
        var tax=(t?.times(0.1))
        var sub= t?.minus(tax!!)
        txtSubTotal.text="$sub"
        txtTax.text="$tax"
        txtTotal.text=bill

        txtOrderId.text=orderId
        txtItemNO.text="Item:$totalItems"
        txtMoney.text=bill
        txtAddress.text=address

        txtCustomerName.text=name
        txtCustomerMobile.text=mobile
       // val commonUrl="http://www.techblr.xyz/admin/img/"

    }
    private fun allProducts(oid:String){
        val queue = Volley.newRequestQueue(this)
        val url ="http://techblr.xyz/admin/restaurant_orderedProducts/"
        var uid="?id=$oid"
        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url + uid, null, Response.Listener {
                try {

                    val data=it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val menuItem = data.getJSONObject(i)
                        val orderItems = AllProducts(
                            menuItem.getString("product_id"),
                            menuItem.getString("product"),
                            menuItem.getString("image"),
                            menuItem.getString("product_price"),
                            menuItem.getString("product_quantity"),
                            menuItem.getString("time")
                        )

                        orderDetails.add(orderItems)

                        recyclerAdapter = AllProductsAdapter(this, orderDetails)
                        recyclerOrder.adapter = this.recyclerAdapter
                        recyclerOrder.layoutManager = this.layoutManager
                    }


                } catch (e: JSONException) {
                    Toast.makeText(
                        this@OrderHistoryActivity,
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