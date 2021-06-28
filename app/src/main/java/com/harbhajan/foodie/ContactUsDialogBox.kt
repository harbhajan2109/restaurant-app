package com.harbhajan.foodie

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_contact_us_dialog_box.view.*


class ContactUsDialogBox :DialogFragment() {
    lateinit var txtPhn1: TextView
    lateinit var txtPhn2: TextView
    lateinit var txtWhatsApp: TextView
    private val requestCall = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_contact_us_dialog_box, container, false)
        txtPhn1=view.findViewById(R.id.txtPhn1)
        txtPhn2=view.findViewById(R.id.txtPhn2)
        txtWhatsApp=view.findViewById(R.id.txtWhatsApp)
        txtPhn1.setOnClickListener {
            val number="+91 9059484841"
            makePhoneCall(number)
        }
        txtPhn2.setOnClickListener {
            val number="+91 9603894841"
            makePhoneCall(number)
        }
        view.txtWhatsApp.setOnClickListener {
            val contact = "+91 9059484841" // use country code with your phone number

            val url = "https://api.whatsapp.com/send?phone=$contact"
            try {
                val pm = context!!.packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    activity as Context,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
        return view
    }
    private fun makePhoneCall( number:String) {
        //val number: String = txtPhn1.text.toString()
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    activity as Context,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permission = arrayOf(android.Manifest.permission.CALL_PHONE)
                //run time popup for request
                requestPermissions(permission, requestCall)
            } else {
                val dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(activity as Context, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (txtPhn1.callOnClick()){
                    val number=txtPhn1.text.toString()
                    makePhoneCall(number)
                }
                if (txtPhn2.callOnClick()){
                    val number=txtPhn2.text.toString()
                    makePhoneCall(number)
                }

            } else {
                Toast.makeText(activity as Context, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}