package buncheez.pk.kitchenapp.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.model.viewOrderModel.IteminfoItem;
import java.util.List;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolder> {
    private List<IteminfoItem> items;
    private Context context;

    public ViewOrderAdapter(Context applicationContext, List<IteminfoItem> itemArrayList) {
        this.context = applicationContext;
        this.items = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_food_view_order_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        if (items.get(i).getAddons()==1){
            viewHolder.productName.setText(items.get(i).getProductName());

            for (int p=0;p<items.get(i).getAddonsinfo().size();p++){
                viewHolder.addOnName.append(items.get(i).getAddonsinfo().get(p).getAddonsName()+"\n");
                viewHolder.addOnQty.append(items.get(i).getAddonsinfo().get(p).getAddOnQty()+"\n");}

        }
        else {
            viewHolder.productName.setText(items.get(i).getProductName());
        }
        viewHolder.sizeTv.setText(items.get(i).getVarientname());
        viewHolder.qty.setText(String.valueOf(items.get(i).getItemqty()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, qty, sizeTv;
        TextView addOnName,addOnQty;
        public ViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productNameId);
            qty = view.findViewById(R.id.quantityId);
            sizeTv = view.findViewById(R.id.sizeId);
            addOnName = view.findViewById(R.id.addOnNameId);
            addOnQty = view.findViewById(R.id.addOnQtyId);

        }
    }
}

