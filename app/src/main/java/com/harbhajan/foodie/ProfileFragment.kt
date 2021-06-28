package com.harbhajan.foodie

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.txtNameProfile
import okhttp3.internal.Version
import java.lang.Exception
import java.util.jar.Manifest


class ProfileFragment : Fragment() {
    companion object{
        private val Image_Pick_Code = 1000;
        private val Permission_Code=1001;
    }
    lateinit var imgBurgerProfile:ImageView
    lateinit var imgProfilePic:CircleImageView
    lateinit var etNameProfile:EditText
    lateinit var etHandleProfile:EditText
    lateinit var etMobileProfile:EditText
    lateinit var etDescriptionProfile:EditText
    lateinit var sharedPreferences: SharedPreferences
    lateinit var saveProfile:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        imgBurgerProfile = view.findViewById(R.id.imgBurgerProfile)
        imgProfilePic = view.findViewById(R.id.imgProfilePic)
        etNameProfile = view.findViewById(R.id.txtNameProfile)
        etHandleProfile = view.findViewById(R.id.txtHandleProfile)
        etMobileProfile = view.findViewById(R.id.txtMobileProfile)
        etDescriptionProfile = view.findViewById(R.id.txtDescriptionProfile)
        saveProfile=view.findViewById(R.id.btnSaveProfile)
        sharedPreferences =
            context?.getSharedPreferences(
                getString(R.string.profile_credentials),
                Context.MODE_PRIVATE
            )!!
        imgProfilePic.setOnClickListener {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(checkSelfPermission(activity as Context,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permission= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    //run time popup for request
                    requestPermissions(permission,Permission_Code)
                }else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }else{
                // android version<=Marshmello
                pickImageFromGallery()
            }
        }

        saveProfile.setOnClickListener {
            val name=etNameProfile.text.toString()
            val handle=etHandleProfile.text.toString()
            val mobile=etMobileProfile.text.toString()
            val description=etDescriptionProfile.text.toString()
            if (name!="" && handle!="" && mobile!=""&& description!="") {

                if (mobile.length == 10) {
                    setSharedPreferences()
                    Toast.makeText(activity as Context, "Profile Saved", Toast.LENGTH_SHORT).show()
                   // sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                    val intent= Intent(activity as Context,SavedProfileActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(activity as Context, "wrong number", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity as Context, "SomeThing Went Wrong!!", Toast.LENGTH_SHORT).show()
            }
        }

            return view
    }

    private fun pickImageFromGallery() {
        //intent to pick image
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, Image_Pick_Code)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            Permission_Code->{
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }else{
                    //permission denied
                    Toast.makeText(activity as Context,"Permission Denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== Activity.RESULT_OK && requestCode== Image_Pick_Code){
            try {

                val mImgUri=data?.data

                // Saves image URI as string to Default Shared Preferences
                sharedPreferences.edit().putString("image", mImgUri.toString()).apply()

                // Sets the ImageView with the Image URI
                imgProfilePic.setImageURI(mImgUri)
                // imgProfile.invalidate()

            }catch (e: Exception){
                Toast.makeText(activity as Context,"$e",Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun setSharedPreferences() {
        sharedPreferences.edit()
            .putString("name", etNameProfile.text.toString()).apply()
        sharedPreferences.edit()
            .putString("mobile", etMobileProfile.text.toString()).apply()
        sharedPreferences.edit()
            .putString("handle", etHandleProfile.text.toString())
            .apply()
        sharedPreferences.edit().putString("description",etDescriptionProfile.text.toString()).apply()

    }
}