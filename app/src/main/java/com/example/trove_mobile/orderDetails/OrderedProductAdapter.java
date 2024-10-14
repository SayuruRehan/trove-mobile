// com/example/mobile_app_client/OrderedProductAdapter.java
package com.example.trove_mobile.orderDetails;

import android.content.Context;
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

import com.example.trove_mobile.R;
import com.example.trove_mobile.product.Product;

import java.util.List;

/**
 * RecyclerView Adapter for displaying ordered products.
 */
public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.OrderedProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public OrderedProductAdapter(List<Product> productList, Context context){
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_product_card, parent, false);
        return new OrderedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedProductViewHolder holder, int position){
        Product product = productList.get(position);

        holder.productNameOrdered.setText(product.getProductName());
        holder.productUnitsOrdered.setText("Units: 1"); // Adjust if you have quantity
        holder.productPriceOrdered.setText("Rs " + product.getPrice());

        // Load product image from Base64 string
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                String base64Image = product.getImage().split(",")[1];
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.productImageOrdered.setImageBitmap(decodedByte);
            } catch (Exception e) {
                e.printStackTrace();
                holder.productImageOrdered.setImageResource(R.drawable.product_image); // Placeholder image
            }
        } else {
            holder.productImageOrdered.setImageResource(R.drawable.product_image); // Placeholder image
        }
    }

    @Override
    public int getItemCount(){
        return productList.size();
    }

    public class OrderedProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImageOrdered;
        TextView productNameOrdered, productUnitsOrdered, productPriceOrdered;

        public OrderedProductViewHolder(@NonNull View itemView){
            super(itemView);

            productImageOrdered = itemView.findViewById(R.id.productImageOrdered);
            productNameOrdered = itemView.findViewById(R.id.productNameOrdered);
            productUnitsOrdered = itemView.findViewById(R.id.productUnitsOrdered);
            productPriceOrdered = itemView.findViewById(R.id.productPriceOrdered);
        }
    }
}
