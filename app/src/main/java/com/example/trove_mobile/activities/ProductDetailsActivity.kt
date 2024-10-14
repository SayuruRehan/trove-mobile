// IT21171338 - TENNAKOON T. M. T. C.-  PRODUCT DETAILS ACTIVITY

package com.example.trove_mobile.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.trove_mobile.R
import com.example.trove_mobile.models.EcommerceItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_home, findViewById(R.id.bottom_navigation))
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_details)

        // Get references to the views
        val itemImage = findViewById<ImageView>(R.id.item_image)
        val itemName = findViewById<TextView>(R.id.item_name)
        val itemDescription = findViewById<TextView>(R.id.item_description)
        val itemPrice = findViewById<TextView>(R.id.item_price)
        val itemStockLevel = findViewById<TextView>(R.id.item_stock_level)

        // Get the item data from the intent
        val item = intent.getSerializableExtra("item") as EcommerceItem

        // Set the item details
        Glide.with(this)
            .load(item.imageUrl)  // Load the image URL
            .into(itemImage)       // Set the image into the ImageView

        itemName.text = item.productName
        itemDescription.text = item.description
        itemPrice.text = "Price: Rs. ${item.productPrice}"
        itemStockLevel.text = "Stock Level: ${item.stock}"



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
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        }
    }

