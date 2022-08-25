package buncheez.pk.kitchenapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.interfaces.AcceptReject;
import buncheez.pk.kitchenapp.model.onlineOrderViewModel.FoodinfoItem;
import buncheez.pk.kitchenapp.utils.SharedPref;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineOrderAdapter extends RecyclerView.Adapter<OnlineOrderAdapter.ViewHolder> {


    private List<FoodinfoItem> items;
    private Context context;
    AcceptReject acceptReject;

    public OnlineOrderAdapter(Context applicationContext, List<FoodinfoItem> itemArrayList, AcceptReject acceptReject) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.acceptReject = acceptReject;
        SharedPref.init(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.design_online_order, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.food.setText(items.get(i).getFoodName());
        viewHolder.qty.setText(items.get(i).getQty());
        if (items.get(i).getAddons()==1){
            for (int t=0;t<items.get(i).getAddonslist().size();t++){
                viewHolder.food.append("\n"+items.get(i).getAddonslist().get(t).getAodonsname());
                viewHolder.qty.append("\n"+items.get(i).getAddonslist().get(t).getAodonsqty());
            }
        }
        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptReject.accept(items.get(i).getFoodID());
                Log.wtf("FoodId",items.get(i).getFoodID());
                Log.wtf("Kitchen",SharedPref.read("kITCHENID", ""));
                Log.wtf("id",SharedPref.read("ID", ""));
            }
        });
        viewHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptReject.reject(items.get(i).getFoodID());
                Log.wtf("FoodId",items.get(i).getFoodID());
                Log.wtf("Kitchen",SharedPref.read("kITCHENID", ""));
                Log.wtf("id",SharedPref.read("ID", ""));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.food)
        TextView food;
        @BindView(R.id.qty)
        TextView qty;
        @BindView(R.id.accept)
        ImageView accept;
        @BindView(R.id.reject)
        ImageView reject;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

