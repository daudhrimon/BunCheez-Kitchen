package buncheez.pk.kitchenapp.retrofit;

import android.content.Context;
import buncheez.pk.kitchenapp.utils.SharedPref;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
    public static Retrofit getRetrofit(Context context) {
        SharedPref.init(context);
        return new Retrofit.Builder()
                .baseUrl(SharedPref.read("BASEURL",""))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
