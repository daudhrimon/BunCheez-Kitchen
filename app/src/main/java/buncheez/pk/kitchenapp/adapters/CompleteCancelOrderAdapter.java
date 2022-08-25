package buncheez.pk.kitchenapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.activities.ViewOrderActivity;
import buncheez.pk.kitchenapp.model.completeCancelOrder.OrderinfoItem;
import java.util.List;

public class CompleteCancelOrderAdapter extends RecyclerView.Adapter<CompleteCancelOrderAdapter.ViewHolder> {

    private List<OrderinfoItem> items;
    private Context context;

    public CompleteCancelOrderAdapter(Context applicationContext, List<OrderinfoItem> itemArrayList) {
        this.context = applicationContext;
        this.items = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_complete_cancel_order_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.orderId.setText(items.get(i).getOrderId());
        viewHolder.customerName.setText(items.get(i).getCustomerName());
        viewHolder.table.setText(items.get(i).getTableName());
        viewHolder.date.setText(items.get(i).getOrderDate());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, customerName, table, date, amount;
        ImageView action;

        public ViewHolder(View view) {
            super(view);
            orderId = view.findViewById(R.id.orderId);
            customerName = view.findViewById(R.id.customerId);
            table = view.findViewById(R.id.tableId);
            date = view.findViewById(R.id.dateId);
            action = view.findViewById(R.id.actionId);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, ViewOrderActivity.class);
                    intent.putExtra("ORDERID",items.get(pos).getOrderId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}

