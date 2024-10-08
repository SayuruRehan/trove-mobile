package com.example.trove_mobile.adapters

import android.content.Context
import android.content.Intent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Import Glide
import com.example.trove_mobile.R
import com.example.trove_mobile.activities.ProductDetailsActivity
import com.example.trove_mobile.models.EcommerceItem
import com.squareup.picasso.Picasso

class EcommerceItemAdapter(private val context: Context ,private val items: List<EcommerceItem>) : RecyclerView.Adapter<EcommerceItemAdapter.EcommerceItemViewHolder>() {

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

        Log.d("EcommerceAdapter", "Items: ${item}");

        // Debugging logs
        Log.d("EcommerceAdapter","Binding item: ${item.productName}, Price: ${item.productPrice}, Image URL: ${item.imageUrl}")

        // Set product details
        holder.productName.text = item.productName
        holder.productPrice.text = "Rs. ${item.productPrice}"

        // Load image using Glide
        Glide.with(holder.productImage.context)
            .load(item.imageUrl) // Assuming item.imageUrl is the Cloudinary URL
            .into(holder.productImage)

        holder.addToCartButton.setOnClickListener {
            // Handle add to cart logic
            Toast.makeText(holder.itemView.context, "${item.productName} added to cart", Toast.LENGTH_SHORT).show()
        }

        // Set item data
        Picasso.get().load(item.imageUrl).into(holder.productImage)
        holder.productName.text = item.productName
        holder.productPrice.text = "Price: $${item.productPrice}"

        // Set click listener
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("item", item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
