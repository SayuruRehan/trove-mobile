package com.example.trove_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trove_mobile.R
import com.example.trove_mobile.models.EcommerceItem

class EcommerceItemAdapter(private val items: List<EcommerceItem>) : RecyclerView.Adapter<EcommerceItemAdapter.EcommerceItemViewHolder>() {

    class EcommerceItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val addToCartButton: Button = itemView.findViewById(R.id.add_to_cart_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EcommerceItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_card, parent, false)
        return EcommerceItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: EcommerceItemViewHolder, position: Int) {
        val item = items[position]

        // Set product details
        holder.productName.text = item.name
        holder.productPrice.text = "$${item.price}"
        holder.productImage.setImageResource(item.imageResId) // Replace with image loading library for real-world apps

        holder.addToCartButton.setOnClickListener {
            // Handle add to cart logic
            Toast.makeText(holder.itemView.context, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
