package com.example.morogocustomerapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

//Variable to use for logging to classify app logs
private const val TAG = "CustomerApp"

class Signup : AppCompatActivity() {

    //private lateinit var auth: FirebaseAuth  //For authentication
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //User Sign up through Firebase
       // auth = FirebaseAuth.getInstance()
        Button_Sign_Up.setOnClickListener {
            signUpUser()
        }
    }
    private fun signUpUser(){

        when{
            CompanyName.text.toString().isEmpty() -> {
                CompanyName.error = "Please Enter the Company Name"
                CompanyName.requestFocus()
            }
            ROCNumber.text.toString().isEmpty() -> {
                ROCNumber.error = "Please Enter the ROC Number"
                ROCNumber.requestFocus()
            }
            Address.text.toString().isEmpty() -> {
                Address.error = "Please Enter the Address"
                Address.requestFocus()
            }
            City.text.toString().isEmpty() -> {
                City.error = "Please Enter the City"
                City.requestFocus()
            }
            State.text.toString().isEmpty() -> {
                State.error = "Please Enter the State"
                State.requestFocus()
            }
            PostalCode.text.toString().isEmpty() -> {
                PostalCode.error = "Please Enter the Pin Code"
                PostalCode.requestFocus()
            }
            PostalCode.text.toString().length < 5 -> {
                PostalCode.error = "Please Enter a valid Pin Code"
                PostalCode.requestFocus()
            }
            !Patterns.WEB_URL.matcher(Website.text.toString()).matches() -> {
                Website.error = "Please enter a valid Website"
                Website.requestFocus()
            }
            else -> {
                Log.d(TAG, "Entering User Sign-up: Company Director Details")
                val intent = Intent(this, SignUpCustomerData::class.java)
                intent.putExtra("ROCNumber",ROCNumber.text.toString())
                intent.putExtra("CompanyName",CompanyName.text.toString())
                intent.putExtra("Address",Address.text.toString())
                intent.putExtra("City",City.text.toString())
                intent.putExtra("State",State.text.toString())
                intent.putExtra("PostalCode",PostalCode.text.toString())
                intent.putExtra("Website",Website.text.toString())
                startActivity(intent)
            }
        }
    }
}