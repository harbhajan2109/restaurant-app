package com.harbhajan.foodie

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*


class SavedProfileFragment : Fragment() {
    lateinit var imgBurgerProfile: ImageView
    lateinit var imgProfilePic: CircleImageView
    lateinit var etNameProfile: TextView
    lateinit var etHandleProfile: TextView
    lateinit var etMobileProfile:TextView
    lateinit var etDescriptionProfile: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_saved_profile, container, false)
        imgBurgerProfile = view.findViewById(R.id.imgBurgerProfile)
        imgProfilePic = view.findViewById(R.id.imgProfilePic)
        etNameProfile = view.findViewById(R.id.txtNameProfile)
        etHandleProfile = view.findViewById(R.id.txtHandleProfile)
        etMobileProfile = view.findViewById(R.id.txtMobileProfile)
        etDescriptionProfile = view.findViewById(R.id.txtDescriptionProfile)
        sharedPreferences =
            context?.getSharedPreferences(
                getString(R.string.profile_credentials),
                Context.MODE_PRIVATE
            )!!
        etNameProfile.text=sharedPreferences.getString("name","")
        etHandleProfile.text=sharedPreferences.getString("handle","")
        etMobileProfile.text=sharedPreferences.getString("mobile","")
        etDescriptionProfile.text=sharedPreferences.getString("description","")
        val mImgUri=sharedPreferences.getString("image","")
        imgProfilePic.setImageURI(Uri.parse(mImgUri))
        return view
    }

}