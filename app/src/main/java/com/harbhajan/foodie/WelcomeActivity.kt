package com.harbhajan.foodie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.harbhajan.foodie.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    lateinit var logo:ImageView
    lateinit var anim:Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        logo=findViewById(R.id.imgLogo)
        Handler().postDelayed({
            val mIntent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(mIntent)
            finish()
        }, 4000)
        anim=AnimationUtils.loadAnimation(this,R.anim.animation)
        logo.startAnimation(anim)

    }
}