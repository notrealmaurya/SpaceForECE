package com.maurya.spaceforece

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maurya.spaceforece.databinding.ActivityMainBinding
import com.maurya.spaceforece.databinding.ActivitySplashBinding
import com.maurya.spaceforece.fragments.ChatFragment
import com.maurya.spaceforece.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private var selectedTab = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(activityMainBinding.getRoot())

        bottomNavigation()
    }


    //Bottom Navigation
    private fun bottomNavigation() {

        setBackgroundColor(this, activityMainBinding.profileLayout)

        drawableColor(
            R.drawable.icon_chats,
            R.color.chatIconColor,
            R.color.chatIconColor_20,
            activityMainBinding.chatsIMAGE,
            activityMainBinding.chatsLayout,
            activityMainBinding.chatsTEXT, this
        )

        selectedTab = 1

        //set Home fragment By default
        fragmentManagerFun(supportFragmentManager, ChatFragment())

        activityMainBinding.chatsLayout.setOnClickListener {
            if (selectedTab != 1) {
                fragmentManagerFun(supportFragmentManager, ChatFragment())
                setVisibility(
                    activityMainBinding.profileTEXT
                )

                drawableColorWhenUnSelecting(
                    R.drawable.icon_profile,
                    activityMainBinding.profileIMAGE,
                    activityMainBinding.profileLayout,
                    this
                )

                drawableColor(
                    R.drawable.icon_chats,
                    R.color.chatIconColor,
                    R.color.chatIconColor_20,
                    activityMainBinding.chatsIMAGE,
                    activityMainBinding.chatsLayout,
                    activityMainBinding.chatsTEXT, this
                )
                animationLayout(activityMainBinding.chatsLayout)

                selectedTab = 1
            }
        }

        activityMainBinding.profileLayout.setOnClickListener {
            //check if profile is already selected or not
            if (selectedTab != 3) {

                fragmentManagerFun(supportFragmentManager, ProfileFragment())
                setVisibility(
                    activityMainBinding.chatsTEXT
                )
                drawableColorWhenUnSelecting(
                    R.drawable.icon_chats,
                    activityMainBinding.chatsIMAGE,
                    activityMainBinding.chatsLayout,
                    this
                )

                drawableColor(
                    R.drawable.icon_profile,
                    R.color.profileIconColor,
                    R.color.profileIconColor_20,
                    activityMainBinding.profileIMAGE,
                    activityMainBinding.profileLayout,
                    activityMainBinding.profileTEXT, this
                )

                animationLayout(activityMainBinding.profileLayout)

                selectedTab = 2
            }
        }

        activityMainBinding.chatsLayout.isActivated = true
    }
}