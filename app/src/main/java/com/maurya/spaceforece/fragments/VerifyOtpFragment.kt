package com.maurya.spaceforece.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.maurya.spaceforece.MainActivity
import com.maurya.spaceforece.R
import com.maurya.spaceforece.databinding.FragmentVerifyOtpBinding
import com.maurya.spaceforece.showToast
import java.util.concurrent.TimeUnit


class VerifyOtpFragment : Fragment() {


    private lateinit var fragmentVerifyOtpBinding: FragmentVerifyOtpBinding

    private var mobileNumber: String? = null
    private var verificationId: String? = null

    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentVerifyOtpBinding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        val view = fragmentVerifyOtpBinding.root

        val args = arguments
        if (args != null) {
            mobileNumber = args.getString("mobile")
            verificationId = args.getString("VerificationId")
        }

        firebaseAuth = FirebaseAuth.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        otpBoxFocus()
        listeners()
    }


    private fun listeners() {


        fragmentVerifyOtpBinding.verifyVerifyOtpFragment.setOnClickListener {
            if (fragmentVerifyOtpBinding.otpBox1.text.toString().trim { it <= ' ' }.isEmpty()
                || fragmentVerifyOtpBinding.otpBox2.text.toString().trim { it <= ' ' }.isEmpty()
                || fragmentVerifyOtpBinding.otpBox3.text.toString().trim { it <= ' ' }.isEmpty()
                || fragmentVerifyOtpBinding.otpBox4.text.toString().trim { it <= ' ' }.isEmpty()
                || fragmentVerifyOtpBinding.otpBox5.text.toString().trim { it <= ' ' }.isEmpty()
                || fragmentVerifyOtpBinding.otpBox6.text.toString().trim { it <= ' ' }.isEmpty()
            ) {
                showToast(requireContext(), "Please Enter valid CODE")
                return@setOnClickListener
            }

            val code: String = fragmentVerifyOtpBinding.otpBox1.text.toString() +
                    fragmentVerifyOtpBinding.otpBox2.text.toString() +
                    fragmentVerifyOtpBinding.otpBox3.text.toString() +
                    fragmentVerifyOtpBinding.otpBox4.text.toString() +
                    fragmentVerifyOtpBinding.otpBox5.text.toString() +
                    fragmentVerifyOtpBinding.otpBox6.text.toString()

            if (verificationId != null) {
                fragmentVerifyOtpBinding.progressVerifyOtpFragment.visibility = View.VISIBLE
                fragmentVerifyOtpBinding.verifyVerifyOtpFragment.visibility = View.INVISIBLE
                val phoneAuthCredential = PhoneAuthProvider.getCredential(
                    verificationId!!, code
                )
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener { task ->
                        fragmentVerifyOtpBinding.progressVerifyOtpFragment.visibility = View.GONE
                        fragmentVerifyOtpBinding.verifyVerifyOtpFragment.visibility = View.VISIBLE
                        if (task.isSuccessful) {
                            val intent = Intent(
                                requireActivity(),
                                MainActivity::class.java
                            )
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        } else {
                            showToast(requireContext(), "OTP is incorrect")
                        }
                    }
            }
        }

        fragmentVerifyOtpBinding.resendOtpVerifyOtpFragment.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(firebaseAuth!!)
                .setPhoneNumber("+91$mobileNumber")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {}
                    override fun onVerificationFailed(e: FirebaseException) {
                        showToast(requireContext(), e.message.toString())
                    }

                    override fun onCodeSent(
                        newVerificationId: String,
                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        verificationId = newVerificationId
                        showToast(requireContext(), "OTP sent")
                    }
                }
                ).build()


            PhoneAuthProvider.verifyPhoneNumber(options)

        }

    }


    private fun otpBoxFocus() {

        fragmentVerifyOtpBinding.otpBox1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    fragmentVerifyOtpBinding.otpBox2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })

        fragmentVerifyOtpBinding.otpBox2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    fragmentVerifyOtpBinding.otpBox3.requestFocus()
                } else {
                    fragmentVerifyOtpBinding.otpBox1.requestFocus()
                    fragmentVerifyOtpBinding.otpBox1.selectAll()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })


        fragmentVerifyOtpBinding.otpBox3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    fragmentVerifyOtpBinding.otpBox4.requestFocus()
                } else {
                    fragmentVerifyOtpBinding.otpBox2.requestFocus()
                    fragmentVerifyOtpBinding.otpBox2.selectAll()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })



        fragmentVerifyOtpBinding.otpBox4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    fragmentVerifyOtpBinding.otpBox5.requestFocus()
                } else {
                    fragmentVerifyOtpBinding.otpBox3.requestFocus()
                    fragmentVerifyOtpBinding.otpBox3.selectAll()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })



        fragmentVerifyOtpBinding.otpBox5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    fragmentVerifyOtpBinding.otpBox6.requestFocus()
                } else {
                    fragmentVerifyOtpBinding.otpBox4.requestFocus()
                    fragmentVerifyOtpBinding.otpBox4.selectAll()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })



        fragmentVerifyOtpBinding.otpBox6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    fragmentVerifyOtpBinding.verifyVerifyOtpFragment.requestFocus()
                } else {
                    fragmentVerifyOtpBinding.otpBox5.requestFocus()
                    fragmentVerifyOtpBinding.otpBox5.selectAll()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }
        })
    }

}