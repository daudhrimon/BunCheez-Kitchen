package buncheez.pk.kitchenapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import buncheez.pk.kitchenapp.MainActivity;
import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.adapters.ViewOrderAdapter;
import buncheez.pk.kitchenapp.model.viewOrderModel.IteminfoItem;
import buncheez.pk.kitchenapp.model.viewOrderModel.ViewOrderResponse;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.SharedPref;
import com.google.gson.Gson;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderActivity extends AppCompatActivity {
    String orderId;
    String TAG = "ViewOrderActivity";
    ChefService ChefService;
    RecyclerView foodCartRecyclerView;
    ImageButton backbtn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#000000"));

        SharedPref.init(this);
        ChefService = AppConfig.getRetrofit(this).create(ChefService.class);
        init();
        foodCartRecyclerView.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this));
        orderId = getIntent().getStringExtra("ORDERID");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewOrderActivity.this, MainActivity.class);
                intent.putExtra("SSS","1");
                startActivity(intent);
                finishAffinity();
            }
        });
        viewOrder();
    }

    private void init() {
        foodCartRecyclerView = findViewById(R.id.foodCartRecyclerViewId);
        backbtn = findViewById(R.id.backId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ViewOrderActivity.this,MainActivity.class);
        intent.putExtra("PENDING","1");
        startActivity(intent);
        finishAffinity();
    }

    private void viewOrder() {
        ChefService.viewOrder(orderId,SharedPref.read("kITCHENID", "")).enqueue(new Callback<ViewOrderResponse>() {
            @Override
            public void onResponse(Call<ViewOrderResponse> call, Response<ViewOrderResponse> response) {
                Log.d(TAG, "onResponse: " + new Gson().toJson(response.body()));
                try {
                    List<IteminfoItem> items = response.body().getData().getIteminfo();
                    foodCartRecyclerView.setAdapter(new ViewOrderAdapter(ViewOrderActivity.this, items));
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<ViewOrderResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.getLocalizedMessage());
            }
        });
    }
}
