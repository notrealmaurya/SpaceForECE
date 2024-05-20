package com.maurya.spaceforece

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.maurya.spaceforece.databinding.ActivitySplashBinding
import com.maurya.spaceforece.fragments.SendOtpFragment

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var activitySplashBinding: ActivitySplashBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = ActivitySplashBinding.inflate(
            layoutInflater
        )
        setContentView(activitySplashBinding.getRoot())

        auth = FirebaseAuth.getInstance()

        Handler(Looper.myLooper()!!).postDelayed(
            {
                if (auth.currentUser != null) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    activitySplashBinding.lotteView.visibility = View.GONE
                    activitySplashBinding.containerSplash.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.containerSplash, SendOtpFragment::class.java, null)
                        .commit()
                }
            }, 3000
        )

    }


}