package buncheez.pk.kitchenapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import buncheez.pk.kitchenapp.activities.LoginActivity;
import buncheez.pk.kitchenapp.activities.SelectKitchen;
import buncheez.pk.kitchenapp.fragments.CompleteOrderFragment;
import buncheez.pk.kitchenapp.fragments.NotificationFragment;
import buncheez.pk.kitchenapp.fragments.OrderListFragment;
import buncheez.pk.kitchenapp.model.loginModel.LoginResponse;
import buncheez.pk.kitchenapp.model.notificationModel.NotificationResponse;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.SharedPref;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    private FragmentManager manager;
    LoginResponse loginResponse;
    Gson gson;
    String id;
    ChefService chefService;
    int notificationSize;
    Toolbar toolbar;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.parseColor("#ed1f24"));

        SharedPref.init(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Order List");
        setSupportActionBar(toolbar);
        chefService = AppConfig.getRetrofit(this).create(ChefService.class);

        SharedPref.write("OVI", "");
        id = SharedPref.read("ID", "");
        TextView poweredBy = findViewById(R.id.poweredId);
        gson = new Gson();
        loginResponse = gson.fromJson(SharedPref.read("LOGINRESPONSE", ""), LoginResponse.class);
        poweredBy.setText(loginResponse.getData().getPowerBy());
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        manager = getSupportFragmentManager();
        OrderListFragment orderListFragment = new OrderListFragment();
        Log.d("sdfsdf", "onCreate: "+getIntent().getStringExtra("OVI"));
        try {
            if (getIntent().getStringExtra("OVI").equals("TSET")) {
                manager.beginTransaction().replace(R.id.content_main, new NotificationFragment(), "ovi").commit();
                toolbar.setTitle("Pending Order");
            } else {
                manager.beginTransaction().add(R.id.content_main, orderListFragment, orderListFragment.getTag())
                        .commit();
                toolbar.setTitle("Order List");
            }

        } catch (Exception e) {
            try {
                if (getIntent().getStringExtra("SSS").equals("1")) {
                    manager.beginTransaction().replace(R.id.content_main, new CompleteOrderFragment(), "ovi").commit();
                    toolbar.setTitle("Complete Order");
                } else {
                    manager.beginTransaction().add(R.id.content_main, orderListFragment, orderListFragment.getTag())
                            .commit();
                    toolbar.setTitle("Order List");
                }
            } catch (Exception ignored) {
                try {
                    manager.beginTransaction().replace(R.id.content_main, orderListFragment, orderListFragment.getTag())
                            .commit();
                    toolbar.setTitle("Order List");
                } catch (Exception ignore) {
                }
            }
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupHeaderView();

        LocalBroadcastManager.getInstance(this).

                registerReceiver(mMessageReceiver,
                        new IntentFilter("REALDATA"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            allOnlineOrder();
        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        allOnlineOrder();
//        OrderListFragment orderListFragment = new OrderListFragment();
//        try {
//            if (SharedPref.read("OVI", "").equals("TSET")) {
//                manager.beginTransaction().replace(R.id.content_main, new NotificationFragment(), "ovi").commit();
//            } else {
//                manager.beginTransaction().replace(R.id.content_main, orderListFragment, orderListFragment.getTag())
//                        .commit();
//            }
//            SharedPref.write("OVI", "");
//        } catch (Exception e) {
//            try{
//            manager.beginTransaction().replace(R.id.content_main, orderListFragment, orderListFragment.getTag())
//                    .commit();}
//            catch (Exception e1){}
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        OrderListFragment orderListFragment = new OrderListFragment();
        try {
            if (SharedPref.read("OVI", "").equals("TSET")) {
                manager.beginTransaction().replace(R.id.content_main, new NotificationFragment(), "ovi").commit();
                toolbar.setTitle("Pending Order");
            } else {
                manager.beginTransaction().replace(R.id.content_main, orderListFragment, orderListFragment.getTag())
                        .commit();
                toolbar.setTitle("Order List");
            }
            SharedPref.write("OVI", "");
        } catch (Exception e) {
            try {
                manager.beginTransaction().replace(R.id.content_main, orderListFragment, orderListFragment.getTag())
                        .commit();
                toolbar.setTitle("Order List");
            } catch (Exception e2) {
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("sdfghfdsa", "onResume: ");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void setupHeaderView() {

        View headerView = navigationView.getHeaderView(0);
        CircleImageView civProfilePic = headerView.findViewById(R.id.civ_profile_pic);
        TextView tvProfileName = headerView.findViewById(R.id.tv_profile_name);
        TextView tvProfileEmail = headerView.findViewById(R.id.tv_profile_email);
        TextView changeKitchen = headerView.findViewById(R.id.changeKitchen);

        //final TextView menu = headerView.findViewById(R.id.menuId);

        final TextView logout = headerView.findViewById(R.id.logoutId);
        final TextView orderList = headerView.findViewById(R.id.orderlistId);
        final TextView completeOrderList = headerView.findViewById(R.id.completeorderlistId);
        changeKitchen.setText(SharedPref.read("kITCHENNAME", "") + "\t");
        orderList.setBackgroundColor(0x30ffffff);
        //final TextView orderHistory = headerView.findViewById(R.id.orderhistoryId);


        String url = loginResponse.getData().getUserPictureURL();
        if (url != null) {
            Picasso.get().load(url).into(civProfilePic);
        }

        changeKitchen.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SelectKitchen.class);
            intent.putExtra("Change", "1");
            startActivity(intent);
            finishAffinity();
        });
        tvProfileName.setText(loginResponse.getData().getFirstname() + " " + loginResponse.getData().getLastname());
        tvProfileEmail.setText(loginResponse.getData().getEmail());
        orderList.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            orderList.setBackgroundColor(0x30ffffff);
            completeOrderList.setBackgroundColor(0x00000000);
            logout.setBackgroundColor(0x00000000);
            OrderListFragment orderListFragment = new OrderListFragment();
            manager.beginTransaction().replace(R.id.content_main, orderListFragment, orderListFragment.getTag())
                    .commit();
            toolbar.setTitle("Order List");
        });
        completeOrderList.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            completeOrderList.setBackgroundColor(0x30ffffff);
            orderList.setBackgroundColor(0x00000000);
            logout.setBackgroundColor(0x00000000);
            manager.beginTransaction().replace(R.id.content_main, new CompleteOrderFragment(), "ovi")
                    .commit();
            toolbar.setTitle("Complete Order");
        });

        logout.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            logout.setBackgroundColor(0x30ffffff);
            orderList.setBackgroundColor(0x00000000);
            completeOrderList.setBackgroundColor(0x00000000);
            logOut();
        });
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure to logout ?");
        builder.setPositiveButton("Yes", (dialog, id) -> {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            SharedPref.write("LOGGEDIN", "No");
            SharedPref.write("kITCHENID", "");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        });

        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    TextView textCartItemCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart) {

            manager.beginTransaction().replace(R.id.content_main, new NotificationFragment(), "ovi").commit();
            toolbar.setTitle("Pending Order");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("poiu", "onPrepareOptionsMenu: ");
        allOnlineOrder();
        return super.onPrepareOptionsMenu(menu);
    }

    private void allOnlineOrder() {
        chefService.allOnlineOrder(id, SharedPref.read("kITCHENID", "")).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                try {
                    SharedPref.write("NOTIFICATION", new Gson().toJson(response.body()));
                    notificationSize = response.body().getData().getOrderinfo().size();
                    textCartItemCount.setText(String.valueOf(notificationSize));
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });
    }
}
