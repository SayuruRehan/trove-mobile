// IT21171338 - TENNAKOON T. M. T. C.- CART VIEW ACTIVITY

package com.example.trove_mobile.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.example.trove_mobile.R
import com.example.trove_mobile.adapters.CartItemAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartViewActivity : AppCompatActivity() {

    private lateinit var cartItemsRecyclerView: RecyclerView
    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var cartItems: List<CartItem>
    private lateinit var checkoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart_view)

        cartItemsRecyclerView = findViewById(R.id.cart_items_recycler_view)
        checkoutButton = findViewById(R.id.button_checkout)

        // Initialize cart items
        cartItems = getCartItems()

        // Setup RecyclerView
        cartItemAdapter = CartItemAdapter(cartItems)
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        cartItemsRecyclerView.adapter = cartItemAdapter

        // Handle checkout button click
        checkoutButton.setOnClickListener {
            showCheckoutDialog()
        }

        // Bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_cart -> {
                    val intent = Intent(this, CartViewActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_account -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun showCheckoutDialog() {
        // Create an AlertDialog to show checkout confirmation
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to confirm checkout?")
            .setCancelable(false)
            .setPositiveButton("Confirm") { dialog, id ->
                // Show success message
                Toast.makeText(this, "Checkout Successful", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                // Dismiss dialog on cancel
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun getCartItems(): List<CartItem> {
        // Return your list of cart items (this is just a placeholder)
        return listOf(
            CartItem("Red Duffel Bag", 3500.00, 1)
        )
    }

    data class CartItem(val name: String, val price: Double, val quantity: Int)
}
