package com.example.trove_mobile.Home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobile_app_client.R;
import com.example.trove_mobile.product.Product;
import com.example.trove_mobile.product.ProductViewActivity;

import java.util.List;

public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;

    public HomeAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        }

        // Get references to views in item_home.xml
        ImageView productImage = convertView.findViewById(R.id.productImage);
        TextView productName = convertView.findViewById(R.id.productName);
        TextView productPrice = convertView.findViewById(R.id.productPrice);

        // Get the current product
        Product product = productList.get(position);

        // Set product name and price
        productName.setText(product.getProductName());
        productPrice.setText(String.format("LKR %s", product.getPrice()));

        // Decode Base64 image and display it
        String base64Image = product.getImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            if (base64Image.contains(",")) {
                base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
            }
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            productImage.setImageBitmap(decodedByte);
        } else {
            productImage.setImageResource(R.drawable.placeholder_image);
        }

        // Handle product click
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductViewActivity.class);
            intent.putExtra("selectedProductId", product.getProductId());
            context.startActivity(intent);
        });

        return convertView;
    }
}