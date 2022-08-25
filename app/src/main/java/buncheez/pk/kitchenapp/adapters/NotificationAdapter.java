package buncheez.pk.kitchenapp.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.activities.OnlineOrderViewActivity;
import buncheez.pk.kitchenapp.model.notificationModel.OrderinfoItem;
import buncheez.pk.kitchenapp.utils.NotificationInterface;
import buncheez.pk.kitchenapp.utils.SharedPref;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<OrderinfoItem> items;
    private Context context;
    NotificationInterface notificationInterface;

    public NotificationAdapter(Context applicationContext, List<OrderinfoItem> itemArrayList, NotificationInterface notificationInterface) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.notificationInterface = notificationInterface;
        SharedPref.init(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.design_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.orderNo.setText(items.get(i).getOrderid());
        viewHolder.custName.setText(items.get(i).getCustomer());
        viewHolder.amount.setText(SharedPref.read("CURRENCY", "") + items.get(i).getAmount());

        //viewHolder.accept.setOnClickListener(view -> notificationInterface.acceptOrder(items.get(i).getOrderid()));

        viewHolder.viewId.setOnClickListener(view -> {
            Intent intent=new Intent(context, OnlineOrderViewActivity.class);
            intent.putExtra("Order_id",items.get(i).getOrderid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderNo)
        TextView orderNo;
        @BindView(R.id.custName)
        TextView custName;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.accept)
        ImageView accept;
        @BindView(R.id.viewId)
        ImageView viewId;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

