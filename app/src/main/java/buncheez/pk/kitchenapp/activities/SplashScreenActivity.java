package buncheez.pk.kitchenapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import buncheez.pk.kitchenapp.MainActivity;
import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.utils.SharedPref;

public class SplashScreenActivity extends AppCompatActivity {
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));
        SharedPref.init(this);

        //SharedPref.write("BASEURL", BuildConfig.BASE_URL);
        if (SharedPref.read("BASEURL", "").isEmpty()) {
//            new Handler().postDelayed(() -> {
//                startActivity(new Intent(SplashScreenActivity.this, QrCodeActivity.class));
//            }, 3000);
            navigateToLoginActivity();
        } else {
            if (SharedPref.read("LOGGEDIN", "").equals("YES")) {
                if (SharedPref.read("kITCHENID", "").isEmpty()) {
                    new Handler().postDelayed(() -> {
                        Intent mainIntent = new Intent(SplashScreenActivity.this, SelectKitchen.class);
                        SplashScreenActivity.this.startActivity(mainIntent);
                        SplashScreenActivity.this.finish();
                    }, 3000);
                } else {
                    navigateToMainActivity();
                }
            } else {
                navigateToLoginActivity();
            }
        }
    }

    private void navigateToLoginActivity() {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            SharedPref.write("PP", "pp");
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }, 3000);
    }

    private void navigateToMainActivity() {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            SharedPref.write("PP", "ppp");
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }, 3000);

    }
}
