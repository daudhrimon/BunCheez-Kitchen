package buncheez.pk.kitchenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import buncheez.pk.kitchenapp.MainActivity;
import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.adapters.OnlineOrderAdapter;
import buncheez.pk.kitchenapp.interfaces.AcceptReject;
import buncheez.pk.kitchenapp.model.notificationModel.OrderAcceptResponse;
import buncheez.pk.kitchenapp.model.onlineOrderViewModel.FoodinfoItem;
import buncheez.pk.kitchenapp.model.onlineOrderViewModel.OnlineOrderResponse;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineOrderViewActivity extends AppCompatActivity implements AcceptReject {

    @BindView(R.id.OrderRecyclerViewId)
    RecyclerView OrderRecyclerViewId;
    String orderId;
    ChefService chefService;
    AcceptReject acceptReject;
    @BindView(R.id.floatingActionButton3)
    FloatingActionButton floatingActionButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_order_view);
        ButterKnife.bind(this);
        SharedPref.init(this);
        acceptReject = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Order");

        chefService = AppConfig.getRetrofit(this).create(ChefService.class);
        OrderRecyclerViewId.setLayoutManager(new LinearLayoutManager(this));
        try {
            orderId = getIntent().getStringExtra("Order_id");
            getOnlineOrder(orderId);
        } catch (Exception ignored) {
        }
    }

    private void getOnlineOrder(String orderId) {
        chefService.viewonlineorder(SharedPref.read("ID", ""), orderId, SharedPref.read("kITCHENID", "")).enqueue(new Callback<OnlineOrderResponse>() {
            @Override
            public void onResponse(Call<OnlineOrderResponse> call, Response<OnlineOrderResponse> response) {
                try {
                    if (OrderRecyclerViewId.getVisibility() == View.GONE) {
                        OrderRecyclerViewId.setVisibility(View.VISIBLE);
                    }
                    List<FoodinfoItem> items = response.body().getData().getFoodinfo();
                    OrderRecyclerViewId.setAdapter(new OnlineOrderAdapter(OnlineOrderViewActivity.this, items, acceptReject));
                } catch (Exception e) {
                    if (OrderRecyclerViewId.getVisibility() == View.VISIBLE) {
                        OrderRecyclerViewId.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<OnlineOrderResponse> call, Throwable t) {
                if (OrderRecyclerViewId.getVisibility() == View.VISIBLE) {
                    OrderRecyclerViewId.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.floatingActionButton3)
    public void onViewClicked() {
        startActivity(new Intent(OnlineOrderViewActivity.this, MainActivity.class));
        finishAffinity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(OnlineOrderViewActivity.this, MainActivity.class);
            intent.putExtra("OVI", "TSET");
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void accept(String foodId) {
        chefService.acceptOrder(SharedPref.read("ID", ""), orderId, SharedPref.read("kITCHENID", ""), foodId).enqueue(new Callback<OrderAcceptResponse>() {
            @Override
            public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
                try {
                    Log.d("poiu", "onResponse: " + new Gson().toJson(response.body()));
                    if (response.body().getStatusCode() == 1) {
                        Toast.makeText(OnlineOrderViewActivity.this, "Item Received Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OnlineOrderViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    getOnlineOrder(orderId);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {
                Log.d("poiuy", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void reject(String foodId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.alert_dialog_reject, null);
        builder.setView(customView);

        EditText cancelReason = customView.findViewById(R.id.cancel_reason);
        Button submit = customView.findViewById(R.id.cancel_button);
        AlertDialog alert = builder.create();
        submit.setOnClickListener(view -> {
            if (TextUtils.isEmpty(cancelReason.getText().toString())) {
                cancelReason.setError("Please fill this field");
                cancelReason.requestFocus();
            } else {
                chefService.cancelorder(orderId, cancelReason.getText().toString(), foodId).enqueue(new Callback<OrderAcceptResponse>() {
                    @Override
                    public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
                        try {
                            alert.dismiss();
                            if (response.body().getStatusCode() == 1) {
                                Toast.makeText(OnlineOrderViewActivity.this, "Item Cancelled Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OnlineOrderViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                            getOnlineOrder(orderId);
                        } catch (Exception ignored) {
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {
                        Log.d("poiuy", "onFailure: " + t.getLocalizedMessage());
                        alert.dismiss();
                    }
                });
            }
        });
        alert.show();

    }


}