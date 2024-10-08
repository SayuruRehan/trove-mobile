// IT21171338 - TENNAKOON T. M. T. C.-  CARTITEM ADAPTER

package com.example.trove_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trove_mobile.R
import com.example.trove_mobile.activities.CartViewActivity

class CartItemAdapter(private val cartItems: List<CartViewActivity.CartItem>) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.cart_item_name)
        private val itemPriceTextView: TextView = itemView.findViewById(R.id.cart_item_price)
        private val itemQuantityTextView: TextView = itemView.findViewById(R.id.cart_item_quantity)

        fun bind(item: CartViewActivity.CartItem) {
            itemNameTextView.text = item.name
            itemPriceTextView.text = "Price: Rs. ${item.price}"
            itemQuantityTextView.text = item.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_card, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}
