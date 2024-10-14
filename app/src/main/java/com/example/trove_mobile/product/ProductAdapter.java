package com.example.trove_mobile.product;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app_client.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private String categoryName;

    public ProductAdapter(Context context, List<Product> products, String categoryName) {
        this.context = context;
        this.productList = products;
        this.categoryName = categoryName;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getProductName());

        String fullDescription = product.getProductDescription();
        int maxDescriptionLength = 50;
        if (fullDescription.length() > maxDescriptionLength) {
            holder.descriptionTextView.setText(fullDescription.substring(0, maxDescriptionLength) + "...");
        } else {
            holder.descriptionTextView.setText(fullDescription);
        }
        holder.priceTextView.setText(String.format("LKR %s", product.getPrice()));

        // Decode and set the product image
        String base64Image = product.getImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            if (base64Image.contains(",")) {
                base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
            }

            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedByte);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image);
        }

        // Handle product click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductViewActivity.class);
            intent.putExtra("selectedProductId", product.getProductId());
            intent.putExtra("selectedCategoryName", categoryName );
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView descriptionTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            nameTextView = itemView.findViewById(R.id.productName);
            priceTextView = itemView.findViewById(R.id.productPrice);
            descriptionTextView = itemView.findViewById(R.id.productDescription);
        }
    }
}
