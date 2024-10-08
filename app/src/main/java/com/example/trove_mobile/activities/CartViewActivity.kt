package com.example.trove_mobile.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trove_mobile.R
import com.example.trove_mobile.adapters.CartItemAdapter

class CartViewActivity : AppCompatActivity() {

    private lateinit var cartItemsRecyclerView: RecyclerView
    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var cartItems: List<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart_view)

        cartItemsRecyclerView = findViewById(R.id.cart_items_recycler_view)

        // Initialize cart items
        cartItems = getCartItems()

        // Setup RecyclerView
        cartItemAdapter = CartItemAdapter(cartItems)
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        cartItemsRecyclerView.adapter = cartItemAdapter
    }

    private fun getCartItems(): List<CartItem> {
        // Return your list of cart items (this is just a placeholder)
        return listOf(

            CartItem("Item 2", 15.50, 2),
            CartItem( "Item 3", 13.00, 3),
            CartItem("Item 1", 10.00, 1),
            CartItem("Item 2", 15.50, 2),
            CartItem( "Item 3", 13.00, 3)
        )
    }

    data class CartItem(val name: String, val price: Double, val quantity: Int)
}
