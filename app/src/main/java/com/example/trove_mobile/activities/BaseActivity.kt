package com.example.trove_mobile.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trove_mobile.R
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the common layout that includes bottom navigation
        setContentView(R.layout.activity_base)

        // Set up bottom navigation (common for activities that extend BaseActivity)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    // Navigate to home
                    true
                }
                R.id.navigation_cart -> {
                    // Navigate to cart
                    val intent = Intent(this, CartViewActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_account -> {
                     true
                }
                // Add other navigation options
                else -> false
            }
        }
    }
}
