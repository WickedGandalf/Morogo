package com.example.morogocustomerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.Email
import kotlinx.android.synthetic.main.activity_main.Password
import kotlinx.android.synthetic.main.activity_sign_up_customer_data.*
import java.lang.NullPointerException

private const val TAG = "CustomerApp"
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Display Landing Page
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        //Action when user clicks the Sign Up button
        Button_Sign_Up.setOnClickListener {
            Log.d(TAG, "Entering User Sign-up: Company Details")
            startActivity(Intent(this, Signup::class.java))
            finish()
        }

        Button_Log_In.setOnClickListener {
            if(!Patterns.EMAIL_ADDRESS.matcher(Email.text.toString()).matches()){
                ErrorMessage.text = "Please Enter a valid Email ID"
                ErrorMessage.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(Email.text.toString().isEmpty() || Password.text.toString().isEmpty()){
                ErrorMessage.text = "Please Enter the Email ID and Password"
                ErrorMessage.visibility = View.VISIBLE
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(Email.text.toString(), Password.text.toString())
                .addOnCompleteListener(this) {task ->
                    if(task.isSuccessful) {
                        Log.d(TAG, "User: Email ID ${Email.text}:sign in: SUCCESS")
                        val user = auth.currentUser
                        startActivity(Intent(this, HomeScreen::class.java))
                    }
                    else {
                        Log.w(TAG, "User: Email ID ${Email.text}:sign in: FAILURE: Error Message:", task.exception )
                        Toast.makeText(baseContext, "Authentication Failed. ", Toast.LENGTH_SHORT).show()
                            ErrorMessage.text = task.exception?.message.toString()
                            ErrorMessage.visibility = View.VISIBLE
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

}