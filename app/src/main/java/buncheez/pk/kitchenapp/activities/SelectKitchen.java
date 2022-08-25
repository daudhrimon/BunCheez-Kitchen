package buncheez.pk.kitchenapp.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.adapters.SelectKitchenAdapter;
import buncheez.pk.kitchenapp.model.loginModel.LoginResponse;
import buncheez.pk.kitchenapp.model.loginModel.KitchenlistItem;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.SharedPref;
import com.google.gson.Gson;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectKitchen extends AppCompatActivity {

    @BindView(R.id.selectKitchen)
    RecyclerView selectKitchen;
    ChefService chefService;
    LinearLayout layoutId ;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_kitchen);
        getWindow().setStatusBarColor(Color.parseColor("#ed1f24"));

        ButterKnife.bind(this);
        SharedPref.init(this);
        layoutId = findViewById(R.id.layoutId);
        try {
            if (getIntent().getStringExtra("Change").equals("1")) {
                getSupportActionBar().setTitle("Change Kitchen");
            } else {
                getSupportActionBar().setTitle("Select Kitchen");
            }
        } catch (Exception e) {
            getSupportActionBar().setTitle("Select Kitchen");
        }
        chefService = AppConfig.getRetrofit(this).create(ChefService.class);
        selectKitchen.setLayoutManager(new LinearLayoutManager(this));
        getKitchenList();
    }

    private void getKitchenList() {
        Log.d("IDOFShape",""+SharedPref.read("ID",""));
        chefService.kitchenlist(SharedPref.read("ID", "")).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    List<KitchenlistItem> items = response.body().getData().getKitchenlist();
                    Log.d("KitchenItems",""+new Gson().toJson(items));
                    if(items.size()>0){
                        layoutId.setVisibility(View.INVISIBLE);
                        selectKitchen.setVisibility(View.VISIBLE);
                        selectKitchen.setAdapter(new SelectKitchenAdapter(SelectKitchen.this, items));
                    }
                    else {
                        layoutId.setVisibility(View.VISIBLE);
                        selectKitchen.setVisibility(View.INVISIBLE);
                    }

                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }
}