package com.example.trove_mobile.activities

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
    }
}