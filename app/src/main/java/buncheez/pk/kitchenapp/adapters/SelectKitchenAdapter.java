package buncheez.pk.kitchenapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import buncheez.pk.kitchenapp.MainActivity;
import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.activities.SelectKitchen;
import buncheez.pk.kitchenapp.model.loginModel.KitchenlistItem;
import buncheez.pk.kitchenapp.utils.SharedPref;
import java.util.List;

public class SelectKitchenAdapter extends RecyclerView.Adapter<SelectKitchenAdapter.ViewHolder> {
    private List<KitchenlistItem> items;
    private Context context;

    public SelectKitchenAdapter(Context applicationContext, List<KitchenlistItem> itemArrayList) {
        this.context = applicationContext;
        this.items = itemArrayList;
        SharedPref.init(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_select_kitchen, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
       viewHolder.kitchenName.setText(items.get(i).getKitchenname());
        viewHolder.layout.setOnClickListener(view -> {
            SharedPref.write("kITCHENID", items.get(i).getKitchenid());
            SharedPref.write("kITCHENNAME", items.get(i).getKitchenname());
            context.startActivity(new Intent(context, MainActivity.class));
            ((SelectKitchen)context).finishAffinity();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kitchenName;
        LinearLayout layout;
        public ViewHolder(View view) {
            super(view);
            kitchenName = view.findViewById(R.id.kitchen_name);
            layout = view.findViewById(R.id.kitchen_layout);
        }
    }
}

