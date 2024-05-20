package com.maurya.spaceforece.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.maurya.spaceforece.MainActivity
import com.maurya.spaceforece.R
import com.maurya.spaceforece.SplashActivity
import com.maurya.spaceforece.databinding.FragmentSendOtpBinding
import com.maurya.spaceforece.showToast
import java.util.concurrent.TimeUnit


class SendOtpFragment : Fragment() {


    private lateinit var fragmentSenOtpBinding: FragmentSendOtpBinding

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentSenOtpBinding = FragmentSendOtpBinding.inflate(inflater, container, false)
        val view = fragmentSenOtpBinding.root


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id_google))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        listeners()
    }

    private fun listeners() {

        fragmentSenOtpBinding.generateOtp.setOnClickListener {
            if (fragmentSenOtpBinding.mobileNumber.getText().toString().trim { it <= ' ' }
                    .isEmpty()) {
                showToast(requireContext(), "Please Enter Mobile No.")
                return@setOnClickListener
            }

            fragmentSenOtpBinding.progressSendOtpFragment.visibility = View.VISIBLE
            fragmentSenOtpBinding.generateOtp.visibility = View.INVISIBLE
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91" + fragmentSenOtpBinding.mobileNumber.text.toString())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        fragmentSenOtpBinding.progressSendOtpFragment.visibility = View.GONE
                        fragmentSenOtpBinding.generateOtp.visibility = View.VISIBLE
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        fragmentSenOtpBinding.progressSendOtpFragment.visibility = View.GONE
                        fragmentSenOtpBinding.generateOtp.visibility = View.VISIBLE
                        showToast(requireContext(), e.message.toString())
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        fragmentSenOtpBinding.progressSendOtpFragment.visibility = View.GONE
                        fragmentSenOtpBinding.generateOtp.visibility = View.VISIBLE
                        val bundle = Bundle().apply {
                            putString("mobile", fragmentSenOtpBinding.mobileNumber.text.toString())
                            putString("VerificationId", verificationId)
                        }
                        val verifyOtpFragment = VerifyOtpFragment()
                        verifyOtpFragment.arguments = bundle

                        requireActivity().supportFragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.containerSplash, verifyOtpFragment)
                            .commit()

                    }
                }
                ).build()


            PhoneAuthProvider.verifyPhoneNumber(options)

        }

        fragmentSenOtpBinding.gmailSignIn.setOnClickListener {
            googleSignInClient.signOut()
            startActivityForResult(googleSignInClient.signInIntent, 13)
        }

        fragmentSenOtpBinding.microsoftSignIN.setOnClickListener {
            signInWithMicroSoft()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 13 && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWIthGoogle(account.idToken!!)

        }

    }

    private fun firebaseAuthWIthGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(
                        requireActivity(),
                        MainActivity::class.java
                    )
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                showToast(requireContext(), it.localizedMessage!!.toString())
            }


    }

    private fun signInWithMicroSoft() {
        val provider = OAuthProvider.newBuilder("microsoft.com")
        val scopes = listOf("email", "profile")
        provider.scopes = scopes

        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener { authResult ->
                    // User is signed in
                    handleSignInResult(authResult)
                }
                .addOnFailureListener { e ->
                    showToast(requireContext(), e.localizedMessage!!.toString())
                    Log.d("microAuth", e.localizedMessage!!.toString())
                }
        } else {
            auth.startActivityForSignInWithProvider(
                requireActivity(),
                provider.build()
            )
                .addOnSuccessListener { authResult ->
                    handleSignInResult(authResult)
                }
                .addOnFailureListener { e ->
                    showToast(requireContext(), e.localizedMessage!!.toString())
                    Log.d("microAuth", e.localizedMessage!!.toString())
                }
        }
    }

    private fun handleSignInResult(authResult: AuthResult) {
        val user = authResult.user
        showToast(requireContext(), "Signed in as: ${user?.displayName}")
        val intent = Intent(
            requireActivity(),
            MainActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}