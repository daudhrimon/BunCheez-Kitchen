package buncheez.pk.kitchenapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import buncheez.pk.kitchenapp.R;
import buncheez.pk.kitchenapp.adapters.CompleteCancelOrderAdapter;
import buncheez.pk.kitchenapp.model.completeCancelOrder.CompleteCancelResponse;
import buncheez.pk.kitchenapp.model.completeCancelOrder.OrderinfoItem;
import buncheez.pk.kitchenapp.retrofit.AppConfig;
import buncheez.pk.kitchenapp.retrofit.ChefService;
import buncheez.pk.kitchenapp.utils.SharedPref;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteOrderFragment extends Fragment {
    RecyclerView processingOrderRecyclerView;
    ChefService ChefService;
    String id;
    Button previewBtn, nextBtn;
    int start = 0;
    LinearLayout layout;
    SpotsDialog progressDialog;

    public CompleteOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete_order, container, false);
        SharedPref.init(getContext());
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        progressDialog.show();
        ChefService = AppConfig.getRetrofit(getContext()).create(ChefService.class);
        Log.d("sdadfsdg", "onCreateView: fdgydfg");
        id = SharedPref.read("ID", "");
        SharedPref.write("ORDERSTATUS", "4");
        processingOrderRecyclerView = view.findViewById(R.id.processingOrderRecyclerViewId);
        previewBtn = view.findViewById(R.id.previewId);
        layout = view.findViewById(R.id.layoutId);
        nextBtn = view.findViewById(R.id.nextId);
        processingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        getCompleteOrder(start);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start += 10;
                Log.d("TAG", "onClick: " + start);
                getCompleteOrder(start);
                if (0 < start) {
                    previewBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start -= 10;
                getCompleteOrder(start);
                if (start < 10) {
                    previewBtn.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void getCompleteOrder(final int start) {

        Log.d("dfdfdf", "onResponse: " + id + " " + SharedPref.read("kITCHENID", ""));
        ChefService.getCompleteOrder(id, start, SharedPref.read("kITCHENID", "")).enqueue(new Callback<CompleteCancelResponse>() {
            @Override
            public void onResponse(Call<CompleteCancelResponse> call, Response<CompleteCancelResponse> response) {
                // Log.d("CCC", "onResponse: " + response.body().getStatus());
                try {
                    if (response.body().getStatus().equals("success")) {

                        Log.d("dfdfdf", "onResponse: " + response.body().getData().getTotalorder());
                        int starts = start + 10;
                        if (starts > response.body().getData().getTotalorder()) {
                            if (nextBtn.getVisibility() == View.VISIBLE) {
                                nextBtn.setVisibility(View.GONE);
                            }
                        } else {
                            nextBtn.setVisibility(View.VISIBLE);
                        }
                        List<OrderinfoItem> items = response.body().getData().getOrderinfo();
                        processingOrderRecyclerView.setAdapter(new CompleteCancelOrderAdapter(getActivity().getApplicationContext(), items));
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        processingOrderRecyclerView.setVisibility(View.GONE);
                        nextBtn.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.d("dfdfdf", "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<CompleteCancelResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                processingOrderRecyclerView.setVisibility(View.GONE);
                nextBtn.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                Log.d("dfdfdf", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
