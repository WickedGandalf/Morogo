package com.example.morogocustomerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up_customer_data.*
import kotlinx.android.synthetic.main.activity_signup.*

//Variable to use for logging to classify app logs
private const val TAG = "CustomerApp"

class SignUpCustomerData : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth  //For authentication
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_customer_data)

        //User Sign up through Firebase
        auth = FirebaseAuth.getInstance()
        Button_Submit.setOnClickListener {
            signUpUser()
        }
    }
    private fun signUpUser(){
        when{
            CompanyDirectorName.text.toString().isEmpty() -> {
                CompanyDirectorName.error = "Please enter the Company Director's Name"
                CompanyDirectorName.requestFocus()
            }
            IC.text.toString().isEmpty() -> {
                IC.error = "Please enter the IC/Passport number"
                IC.requestFocus()
            }
            (IC.text.toString().length < 7 ||  IC.text.toString().length > 12) -> {
                IC.error = "Please Enter a valid IC/Passport number"
                IC.requestFocus()
            }
            Phone.text.toString().isEmpty() -> {
                Phone.error = "Please enter the Phone number"
                Phone.requestFocus()
            }
            Email.text.toString().isEmpty() -> {
                Email.error = "Please Enter the Email Address"
                Email.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(Email.text.toString()).matches() -> {
                Email.error = "Please Enter a Valid Email Address"
                Email.requestFocus()
            }
            Password.text.toString().isEmpty() -> {
                Password.error = "Please Enter the password"
                Password.requestFocus()
            }
            Password.text.toString().length < 6 -> {
                Password.error = "Password should be at least 6 characters"
                Password.requestFocus()
            }
            else -> {
                val roc = intent.getStringExtra("ROCNumber")
                lateinit var currentUserUID: String
                auth.createUserWithEmailAndPassword(Email.text.toString(), Password.text.toString())
                    .addOnSuccessListener {
                        Log.i(TAG, "Account Created in Firebaase Successfully")
                        //Toast.makeText(baseContext,"Account Created Successfully", Toast.LENGTH_SHORT).show()
                        //startActivity(Intent(this, MainActivity::class.java))
                        currentUserUID = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        val customerData = hashMapOf(
                            "Director Name" to CompanyDirectorName.text.toString(),
                            "IC" to IC.text.toString(),
                            "Phone" to Phone.text.toString(),
                            "Email" to Email.text.toString(),
                            "Password" to Password.text.toString(),
                            "ROCNumber" to intent.getStringExtra("ROCNumber"),
                            "Company Name" to intent.getStringExtra("CompanyName"),
                            "Address" to intent.getStringExtra("Address"),
                            "State" to intent.getStringExtra("State"),
                            "City" to intent.getStringExtra("City"),
                            "Pincode" to intent.getStringExtra("PostalCode"),
                            "Website" to intent.getStringExtra("Website"),
                            "CustomerID" to currentUserUID.toString()
                        )
                        db.collection("Customer Record").document(currentUserUID.toString()).set(customerData, SetOptions.merge())
                            .addOnSuccessListener {
                                Log.d(TAG, "Company Director details added for Company ROC Number: $roc IC: ${IC.text.toString()} added to the database")
                                Toast.makeText(baseContext,"Account Created Successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomeScreen::class.java))
                            }
                            .addOnFailureListener{
                                    e->Log.e(TAG, "Error writing company director details to the database. ROC Number: $roc IC: ${IC.text.toString()}",e)
                                    Toast.makeText(baseContext,"Error creating account: ${e.message.toString()}", Toast.LENGTH_SHORT).show()
                                auth.currentUser?.delete()
                                    ?.addOnCompleteListener{
                                        Log.d(TAG, "Deleting unsuccessful user from Firebase")
                                    }
                            }
                    }
                    .addOnFailureListener{
                            e->Log.e(TAG,"Error creating account",e)
                        val error = e.message.toString()
                        Toast.makeText(baseContext,"Error creating account: $error", Toast.LENGTH_SHORT).show()
                        if(error == "The email address is already in use by another account." ){
                            return@addOnFailureListener
                        }
                        //Rollback the database entry if the account creation is a failure
                        db.collection("Customer Record").document("$roc").delete()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
            }
        }
    }
}