// IT21171338 - TENNAKOON T. M. T. C.-  SIGNUP ACTIVITY

package com.example.trove_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trove_mobile.R
import com.example.trove_mobile.models.api.*
import com.example.trove_mobile.repositories.AuthRepository
import com.example.trove_mobile.utils.TokenManager
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup_page)

        // Adjust the window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeButton = findViewById<MaterialButton>(R.id.button_signup)
        val loginButton = findViewById<TextView>(R.id.linkTextViewSignup)
        val backButton = findViewById<ImageButton>(R.id.imageButtonBackSignup)

        // Initialize AuthRepository
        authRepository = AuthRepository()

        val firstnameEditText = findViewById<EditText>(R.id.editTextFirstnameSignup)
        val lastnameEditText = findViewById<EditText>(R.id.editTextLastnameSignup)
        val emailEditText = findViewById<EditText>(R.id.editTextEmailSignup)
        val phoneEditText = findViewById<EditText>(R.id.editTextPhoneSignup)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordSignup)
        val reenterpasswordEditText = findViewById<EditText>(R.id.editTextReenterPasswordSignup)

        // Handle signup button click
        homeButton.setOnClickListener {
            val firstname = firstnameEditText.text.toString()
            val lastname = lastnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val re_enterpassword = reenterpasswordEditText.text.toString()
            val role = "customer"
            val status = "inactive"

            // Check if fields are not empty
            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && re_enterpassword.isNotEmpty()) {
                val request = RegisterRequest(firstname, lastname, email, phone, password, role, status)

                Log.d("SignupActivity", "request: $request")

                // Perform signup through AuthRepository
                authRepository.signup(request, object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        Log.d("SignupActivity","response: $response")
                        if (response.isSuccessful) {
                            val registerResponse = response.body()
                            registerResponse?.token?.let {
                                TokenManager.saveToken(it)
                            }
                            Toast.makeText(this@SignupActivity, "Register successful!", Toast.LENGTH_SHORT).show()

                            // Navigate to HomeActivity
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@SignupActivity, "Register failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@SignupActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to login page
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginRequest::class.java)
            startActivity(intent)
        }

        // Back button functionality
        backButton.setOnClickListener {
            finish() // Close this activity to go back to the previous screen
        }
    }
}
