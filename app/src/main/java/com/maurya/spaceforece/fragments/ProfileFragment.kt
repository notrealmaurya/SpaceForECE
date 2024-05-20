package com.maurya.spaceforece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.maurya.spaceforece.R
import com.maurya.spaceforece.SplashActivity
import com.maurya.spaceforece.databinding.FragmentProfileBinding
import com.maurya.spaceforece.databinding.FragmentSendOtpBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


class ProfileFragment : Fragment() {


    private lateinit var fragmentProfileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = fragmentProfileBinding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val lastSignInTimestamp = user.metadata!!.lastSignInTimestamp
            val date = Date(lastSignInTimestamp)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val formattedDate = sdf.format(date)

            fragmentProfileBinding.userNameProfileFragment.text = user.displayName
            fragmentProfileBinding.emailIdProfileFragment.text = user.email
            fragmentProfileBinding.mobileNumberProfileFragment.text = user.phoneNumber
            fragmentProfileBinding.lastSignInProfileFragment.text =
                "Last Signed In ${formattedDate}"

        }

        fragmentProfileBinding.signOutProfileFragment.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }


    }


}