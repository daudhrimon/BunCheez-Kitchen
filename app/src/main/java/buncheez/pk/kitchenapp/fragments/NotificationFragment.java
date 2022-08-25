package buncheez.pk.kitchenapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.adapters.NotificationAdapter;
import buncheez.pk.kitchenapp.model.notificationModel.NotificationResponse;
import buncheez.pk.kitchenapp.model.notificationModel.OrderinfoItem;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.NotificationInterface;
import buncheez.pk.kitchenapp.utils.SharedPref;
import com.google.gson.Gson;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements NotificationInterface {

    @BindView(R.id.foodNotification)
    RecyclerView foodNotification;
    Unbinder unbinder;
    NotificationInterface notificationInterface;
    String waiterId;
    ChefService chefService;
    int notificationSize;
    @BindView(R.id.layoutId)
    LinearLayout layoutId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        SharedPref.init(getContext());
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        chefService = AppConfig.getRetrofit(getContext()).create(ChefService.class);
        notificationInterface = this;
        waiterId = SharedPref.read("ID", "");
        foodNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        allOnlineOrder();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("REALDATA"));
        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                allOnlineOrder();
            } catch (Exception ignored) {
            }
        }
    };

    @Override
    public void acceptOrder(String orderId) {
        Log.d("poiuy", "acceptOrder: " + waiterId);
         }

    TextView textCartItemCount;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
    }

    private void allOnlineOrder() {
        Log.wtf("Kitchen",SharedPref.read("kITCHENID", ""));
        chefService.allOnlineOrder(waiterId,SharedPref.read("kITCHENID", "")).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                try {
                    Log.d("safsdfsa", "onResponse: "+new Gson().toJson(response.body()));
                    if (response.body().getStatusCode() == 1) {
                        if (layoutId.getVisibility() == View.VISIBLE) {
                            layoutId.setVisibility(View.GONE);
                            foodNotification.setVisibility(View.VISIBLE);
                        }
                        List<OrderinfoItem> list = response.body().getData().getOrderinfo();
                        foodNotification.setAdapter(new NotificationAdapter(getContext(), list, notificationInterface));
                        notificationSize = response.body().getData().getOrderinfo().size();
                        textCartItemCount.setText(String.valueOf(notificationSize));
                    } else {
                        if (layoutId.getVisibility() == View.GONE) {
                            layoutId.setVisibility(View.VISIBLE);
                            foodNotification.setVisibility(View.GONE);
                        }
                        textCartItemCount.setText("0");
                    }
                } catch (Exception e) {
                    Log.d("safsdfsa", "onResponse: "+e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.d("poi", "onFailure: " + t.getLocalizedMessage());
                try{
                if (layoutId.getVisibility() == View.GONE) {
                    layoutId.setVisibility(View.VISIBLE);
                    foodNotification.setVisibility(View.GONE);
                }
                }catch (Exception e){/**/}
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
