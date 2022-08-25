package buncheez.pk.kitchenapp.activities;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.model.loginModel.LoginResponse;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.SharedPref;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button signInBtn;
    EditText emailET, passwordET;
    private ChefService chefService;
    SpotsDialog progressDialog;
    private String TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getToken();

        ButterKnife.bind(this);
        SharedPref.init(this);
        if (SharedPref.read("BASEURL", "").isEmpty()){
            SharedPref.write("BASEURL", getString(R.string.BASE_URL));
        }

        initAll();

        try {
            chefService = AppConfig.getRetrofit(this).create(ChefService.class);
            progressDialog = new SpotsDialog(this, R.style.Custom);

            signInBtn.setOnClickListener(v -> {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches()) {
                    emailET.setError("Enter an email address");
                    emailET.requestFocus();
                    return;
                }
                if (passwordET.getText().toString().length() < 5) {
                    passwordET.setError("Password is too short");
                    passwordET.requestFocus();
                    return;
                }
                if (TOKEN.isEmpty()){
                    Toast.makeText(this, "Something went wrong, in this case your device will not get notifications", Toast.LENGTH_SHORT).show();
                }
                progressDialog.show();
                signIN();
            });

        } catch (Exception e) {
            warning();
        }
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken() //get firebase token
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.wtf(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        TOKEN = task.getResult();
                        FirebaseMessaging.getInstance().subscribeToTopic("global");
                        Toast.makeText(LoginActivity.this,"Device Token Generated Successfully",Toast.LENGTH_SHORT).show();
                        Log.wtf("TOKEN",TOKEN);
                    }
                });
    }

    private void warning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Base Url is not valid.\nPlease again Scan your valid QR Code");
        builder.setPositiveButton("Rescan QR Code", (dialog, id) -> {
            Intent intent = new Intent(LoginActivity.this, QrCodeActivity.class);
            startActivity(intent);

        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void signIN() {
        chefService.doSignIn(emailET.getText().toString(), passwordET.getText().toString(),TOKEN).
                enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        try {
                            Log.d("checkhere","I'm Here"+response);

                            Log.d("ppp", "onResponse: " + response.body().getMessage());
                            LoginResponse loginResponse = response.body();
                            Gson gson = new Gson();
                            String loginResponses = gson.toJson(loginResponse);

                            try {
                                if (response.body().getStatusCode()==1) {
                                    SharedPref.write("LOGINRESPONSE", loginResponses);
                                    SharedPref.write("LOGGEDIN", "YES");
                                    SharedPref.write("ID", loginResponse.getData().getId());
                                    SharedPref.write("CURRENCY", loginResponse.getData().getCurrencysign());
                                    startActivity(new Intent(LoginActivity.this, SelectKitchen.class));
                                    finishAffinity();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Wrong email or Password", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception ignored) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("Onfailll",t.toString());
                        Toast.makeText(LoginActivity.this, "Wrong email or Password", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void initAll() {
        signInBtn = findViewById(R.id.signInBtnId);
        emailET = findViewById(R.id.emailId);
        passwordET = findViewById(R.id.passwordId);
    }

    @OnClick(R.id.reset)
    public void onViewClicked() {
        SharedPref.write("BASEURL", "");
        startActivity(new Intent(LoginActivity.this, QrCodeActivity.class));
    }
}
