// com/example/mobile_app_client/OrderAdapter.java
package com.example.trove_mobile.orderDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trove_mobile.MainActivity;
import com.example.mobile_app_client.R;
import com.example.trove_mobile.order.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying orders.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;

    public OrderAdapter(List<Order> orderList, Context context){
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position){
        Order order = orderList.get(position);

        holder.orderNumber.setText("Order: " + order.getOrderId());
        holder.orderDate.setText(formatDate(order.getOrderDate()));
        holder.orderQuantity.setText("Quantity: " + order.getProductIds().size());
        holder.orderTotalAmount.setText("Total Amount: Rs " + order.getAmount());
        holder.orderStatus.setText(capitalizeFirstLetter(order.getStatus()));

        holder.buttonOrderDetails.setOnClickListener(v -> {
            // Navigate to OrderDetailsFragment
            OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.newInstance(order.getOrderId());

            FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, orderDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    @Override
    public int getItemCount(){
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderNumber, orderDate, orderQuantity, orderTotalAmount, orderStatus;
        Button buttonOrderDetails;

        public OrderViewHolder(@NonNull View itemView){
            super(itemView);

            orderNumber = itemView.findViewById(R.id.orderNumber);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderQuantity = itemView.findViewById(R.id.orderQuantity);
            orderTotalAmount = itemView.findViewById(R.id.orderTotalAmount);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            buttonOrderDetails = itemView.findViewById(R.id.buttonOrderDetails);
        }
    }

    private String formatDate(String dateString){
        try {
            // Assuming the date string is in ISO 8601 format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e){
            e.printStackTrace();
            return dateString;
        }
    }

    private String capitalizeFirstLetter(String str){
        if (str == null || str.length() == 0){
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
