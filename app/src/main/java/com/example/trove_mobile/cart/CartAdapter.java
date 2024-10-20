package com.example.trove_mobile.cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trove_mobile.product.Product;
import com.example.mobile_app_client.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;
    private Runnable updateTotalAmountCallback;

    private OnCartItemDeleteListener deleteListener;

    public CartAdapter(List<CartItem> cartItemList, Context context, Runnable updateTotalAmountCallback, OnCartItemDeleteListener deleteListener) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.updateTotalAmountCallback = updateTotalAmountCallback;
        this.deleteListener = deleteListener;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvPrice, tvQuantity;
        ImageButton btnDelete, btnMinus, btnPlus;

        public CartViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        Product product = item.getProduct();

        // Bind data to the views
        holder.tvProductName.setText(product.getProductName());
        holder.tvPrice.setText(String.format("Rs %.2f", product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        // Decode Base64 image
        String base64Image = product.getImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            // Remove data:image/jpeg;base64, or similar prefix if present
            if (base64Image.contains(",")) {
                base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
            }

            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.ivProductImage.setImageBitmap(decodedByte);
        } else {
            // Set a placeholder image if no image is available
            holder.ivProductImage.setImageResource(R.drawable.product_image);
        }

        // Set content descriptions for accessibility
        holder.btnMinus.setContentDescription(context.getString(R.string.decrease_quantity));
        holder.btnPlus.setContentDescription(context.getString(R.string.increase_quantity));
        holder.btnDelete.setContentDescription(context.getString(R.string.delete_item));

        // Set tooltips for additional accessibility
        TooltipCompat.setTooltipText(holder.btnMinus, context.getString(R.string.decrease_quantity));
        TooltipCompat.setTooltipText(holder.btnPlus, context.getString(R.string.increase_quantity));
        TooltipCompat.setTooltipText(holder.btnDelete, context.getString(R.string.delete_item));

        // Handle quantity decrease
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
                updateTotalAmountCallback.run();
            }
        });

        // Handle quantity increase
        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
            updateTotalAmountCallback.run();
        });

        // Handle item deletion
        holder.btnDelete.setOnClickListener(v -> {
            CartItem itemToDelete = cartItemList.get(position);
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
            updateTotalAmountCallback.run();
            if (deleteListener != null) {
                deleteListener.onCartItemDeleted(itemToDelete);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
