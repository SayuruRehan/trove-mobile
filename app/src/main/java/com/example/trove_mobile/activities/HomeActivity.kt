package com.example.trove_mobile.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trove_mobile.R
import com.example.trove_mobile.adapters.EcommerceItemAdapter
import com.example.trove_mobile.models.EcommerceItem
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Sample data for the RecyclerView
        val items = listOf(
            EcommerceItem("Product 1", 100.0, R.drawable.item_image),
            EcommerceItem("Product 2", 150.0, R.drawable.item_image),
            EcommerceItem("Product 3", 200.0, R.drawable.item_image)
        )

        // Get a reference to the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.item_list)

        // Set the LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set the Adapter
        recyclerView.adapter = EcommerceItemAdapter(items)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Navigate to Home screen
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_cart -> {
                    // Navigate to Cart screen
                    // Add your intent or action here
                    true
                }
                R.id.navigation_account -> {
                    // Navigate to Account screen
                    // Add your intent or action here
                    true
                }
                else -> false
            }
        }


    }
}