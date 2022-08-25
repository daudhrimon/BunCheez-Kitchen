package buncheez.pk.kitchenapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.model.foodReadyModel.FoodReadyResponse;
import buncheez.pk.kitchenapp.model.notificationModel.OrderAcceptResponse;
import buncheez.pk.kitchenapp.model.orderListModel.IteminfoItem;
import buncheez.pk.kitchenapp.model.orderListModel.OrderListResponse;
import buncheez.pk.kitchenapp.model.orderListModel.OrderinfoItem;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.SharedPref;
import com.google.gson.Gson;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends Fragment {
    RecyclerView OrderRecyclerView;
    ChefService chefService;
    String id;
    SpotsDialog progressDialog;
    LinearLayout layout;
    SwipeRefreshLayout layoutRecylecrView;
    SwipeRefreshLayout swipeRefreshLayout;

    Parcelable mListState;
    Bundle mBundleRecyclerViewState;

    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        SharedPref.init(getContext());
        mBundleRecyclerViewState = new Bundle();

        chefService = AppConfig.getRetrofit(getContext()).create(ChefService.class);
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        progressDialog.show();
        id = SharedPref.read("ID", "");
        Log.d("poi", "onCreateView: " + id);
        OrderRecyclerView = view.findViewById(R.id.OrderRecyclerViewId);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        layout = view.findViewById(R.id.layoutId);
        layoutRecylecrView = view.findViewById(R.id.swiperefresh);
        OrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getOrderList();
        });
        getOrderList();
        return view;
    }


    private void getOrderList() {
        chefService.getOrderList(id, SharedPref.read("kITCHENID", "")).enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("ppp", "onResponse: "+new Gson().toJson(response.body()));
                try {
                    if (response.body().getStatus().equals("success")) {
                        Log.d("ppp", "onResponse: " + new Gson().toJson(response.body()));
                        try {
                            if (response.body().getData().getHasitem() == 1) {
                                List<OrderinfoItem> items = response.body().getData().getOrderinfo();
                                OrderRecyclerView.setAdapter(new OrderAdapter(getActivity().getApplicationContext(), items));
                                if (OrderRecyclerView != null) {
                                    new Handler().postDelayed(() -> {
                                        mListState = mBundleRecyclerViewState.getParcelable("KEY_RECYCLER_STATE");
                                        OrderRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                                        Log.d("safdas", "run: " + mListState);

                                    }, 150);

                                }
                            } else {
                                layout.setVisibility(View.VISIBLE);
                                layoutRecylecrView.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            Log.d("ppp", "onResponse: ]" + e.getLocalizedMessage());
                            layout.setVisibility(View.VISIBLE);
                            layoutRecylecrView.setVisibility(View.GONE);
                        }
//                        LinearLayoutManager layoutManager = ((LinearLayoutManager)OrderRecyclerView.getLayoutManager());
//
//                        int visibleItemCount = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition();
//                        Log.d("dsadasd", "onResponse: "+visibleItemCount);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
//                        if (layout.getVisibility() == View.VISIBLE) {
//                            layout.setVisibility(View.GONE);
//                            layoutRecylecrView.setVisibility(View.VISIBLE);
//                        }
                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    layout.setVisibility(View.VISIBLE);
                    layoutRecylecrView.setVisibility(View.GONE);
                    Log.d("ppp", "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                Log.d("ppp", "onFailure: " + t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                layoutRecylecrView.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void orderReady(String orderId, String productId, String variantId, String ready) {
        progressDialog.show();
        chefService.fooReady(orderId, productId, variantId, ready, SharedPref.read("kITCHENID", "")).enqueue(new Callback<FoodReadyResponse>() {
            @Override
            public void onResponse(Call<FoodReadyResponse> call, Response<FoodReadyResponse> response) {
                try {
                    mListState = OrderRecyclerView.getLayoutManager().onSaveInstanceState();
                    mBundleRecyclerViewState.putParcelable("KEY_RECYCLER_STATE", mListState);
                    if (response.body().getData().getIsready().equals("1")) {
                        Log.d("poiuyt", "onResponse: " + new Gson().toJson(response.body()));
                        Toast.makeText(getActivity(), "Food is Ready", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Food is Processing", Toast.LENGTH_SHORT).show();
                    }
                    getOrderList();
                } catch (Exception ignored) {
                    Log.d("poiuyt", "onResponse: " + ignored.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<FoodReadyResponse> call, Throwable t) {
                Log.d("poiuyt", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        private List<IteminfoItem> items;
        private Context context;

        public ItemAdapter(Context applicationContext, List<IteminfoItem> itemArrayList) {
            this.context = applicationContext;
            this.items = itemArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_order_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.productName.setText(items.get(i).getProductName());
            viewHolder.qty.setText(items.get(i).getItemqty());
            viewHolder.notes.append(items.get(i).getItemnote());
            try {
                int size = items.get(i).getAddonsinfo().size();
                for (int j = 0; j < size; j++) {
                    if (Integer.parseInt(items.get(i).getAddonsinfo().get(j).getAddOnQty()) > 0) {
                        viewHolder.addOnName.append(items.get(i).getAddonsinfo().get(j).getAddonsName() + "\n");
                        viewHolder.addOnqty.append(items.get(i).getAddonsinfo().get(j).getAddOnQty() + "\n");
                    }
                }
            } catch (Exception ignored) {
            }
            if (items.get(i).getFoodStatus().equals("1")) {
                viewHolder.checkBox.setChecked(true);
                viewHolder.checkBox.setText("Ready");
            }
            viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (items.get(i).getFoodStatus().equals("1")) {
                    orderReady(items.get(i).getOrderId(), items.get(i).getProductsID(), items.get(i).getVarientid(), "0");
                } else {
                    orderReady(items.get(i).getOrderId(), items.get(i).getProductsID(), items.get(i).getVarientid(), "1");
                }
            });


        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView productName, qty, addOnName, addOnqty,notes;
            CheckBox checkBox;

            public ViewHolder(View view) {
                super(view);
                productName = view.findViewById(R.id.productNameId);
                qty = view.findViewById(R.id.qtyId);
                notes = view.findViewById(R.id.note);
                checkBox = view.findViewById(R.id.checkBoxId);
                addOnName = view.findViewById(R.id.addOnNameId);
                addOnqty = view.findViewById(R.id.addOnqtyId);
            }
        }
    }

    public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

        private List<OrderinfoItem> items;
        private Context context;

        public OrderAdapter(Context applicationContext, List<OrderinfoItem> itemArrayList) {
            this.context = applicationContext;
            this.items = itemArrayList;
        }

        @Override
        public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = mInflater.inflate(R.layout.design_food, viewGroup, false);
            return new OrderAdapter.ViewHolder(view);
        }

        boolean status;

        @SuppressLint({"SetTextI18n", "RecyclerView"})
        @Override
        public void onBindViewHolder(OrderAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.orderId.setText("Order ID: " + items.get(i).getOrderId());
            viewHolder.asd.setLayoutManager(new LinearLayoutManager(context));
            if (!items.get(i).getNotes().isEmpty()) {
                viewHolder.note.setText("Note: " + items.get(i).getNotes());
            }
            try {
                int size = items.get(i).getIteminfo().size();
                if (size == 0) {
                    viewHolder.cardView.setVisibility(View.GONE);
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                } else {
                    viewHolder.cardView.setVisibility(View.VISIBLE);
                }
                List<IteminfoItem> iteminfoItems = items.get(i).getIteminfo();
                viewHolder.asd.setAdapter(new ItemAdapter(context, iteminfoItems));
            } catch (Exception e) {
                Log.d("fdgdfgdfg", "onBindViewHolder: " + e.getLocalizedMessage());
            }
            viewHolder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = true;
                    String iteminfo = "";
                    for (int t = 0; t < items.get(i).getIteminfo().size(); t++) {
                        if (iteminfo.isEmpty()) {
                            iteminfo = items.get(i).getIteminfo().get(t).getProductsID();
                        } else {
                            iteminfo = iteminfo + "," + items.get(i).getIteminfo().get(t).getProductsID();
                        }
                        if (items.get(i).getIteminfo().get(t).getFoodStatus().equals("0")) {
                            status = false;
                        }
                    }
                    if (status == true) {
                        markAsReady(items.get(i).getOrderId(), iteminfo);
                    } else {
                        Toast.makeText(context, "Food is not ready", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        private void markAsReady(String orderId, String foodId) {
            Log.d("sadfsdf", "markAsReady: " + orderId + "\t" + foodId);
            chefService.markasready(orderId, foodId).enqueue(new Callback<OrderAcceptResponse>() {
                @Override
                public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
                    try {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getOrderList();
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView orderId, note;
            Button done;
            RecyclerView asd;
            CardView cardView;

            public ViewHolder(View view) {
                super(view);

                asd = view.findViewById(R.id.asd);
                orderId = view.findViewById(R.id.product);
                done = view.findViewById(R.id.button);
                cardView = view.findViewById(R.id.cardView);
                note = view.findViewById(R.id.notes);


            }
        }
    }


}
