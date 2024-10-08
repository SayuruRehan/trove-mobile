package com.example.trove_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trove_mobile.R
import com.example.trove_mobile.adapters.EcommerceItemAdapter
import com.example.trove_mobile.models.EcommerceItem
import com.example.trove_mobile.repositories.ProductRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productRepository: ProductRepository
    private lateinit var categoryContainer: LinearLayout  // Reference for the category container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        layoutInflater.inflate(R.layout.activity_home, findViewById(R.id.bottom_navigation))
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ProductRepository
        productRepository = ProductRepository()

        // Get a reference to the RecyclerView
        recyclerView = findViewById(R.id.item_list)
        categoryContainer = findViewById(R.id.category_container)

        // Set the LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch products from the backend
        fetchProducts()

        // Dynamically create category buttons
        createCategoryButtons()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_cart -> {
                    // Navigate to Cart screen
                    val intent = Intent(this, CartViewActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_account -> {
                    // Navigate to Account screen
                    true
                }
                else -> false
            }
        }


    }

    private fun createCategoryButtons() {
        // List of categories
        val categories = listOf("Electronics", "Clothing", "Home & Kitchen", "Books", "Beauty")

        // Create buttons for each category
        for (category in categories) {
            val button = MaterialButton(this).apply {
                text = category
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = 10
                    marginEnd = 10
                }
                setBackgroundColor(resources.getColor(R.color.blue)) // Set background color
                setTextColor(resources.getColor(R.color.white)) // Set text color
                textSize = 16f
                cornerRadius = 10
                // Optional: Set an OnClickListener for each button
                setOnClickListener {
                    Toast.makeText(this@HomeActivity, "Clicked: $category", Toast.LENGTH_SHORT).show()
                    // Implement category selection behavior if needed
                }
            }
            categoryContainer.addView(button) // Add button to the container
        }
    }

    private fun fetchProducts() {
        // Replace with your JWT token
        val token = "your_jwt_token_here"

        productRepository.getProducts(token, object : Callback<List<EcommerceItem>> {
            override fun onResponse(call: Call<List<EcommerceItem>>, response: Response<List<EcommerceItem>>) {
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    // Set the Adapter
                    recyclerView.adapter = EcommerceItemAdapter(this@HomeActivity, items)

                } else {
                    Toast.makeText(this@HomeActivity, "Failed to fetch products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EcommerceItem>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
