package buncheez.pk.kitchenapp.retrofit;

import buncheez.pk.kitchenapp.model.completeCancelOrder.CompleteCancelResponse;
import buncheez.pk.kitchenapp.model.foodReadyModel.FoodReadyResponse;
import buncheez.pk.kitchenapp.model.loginModel.LoginResponse;
import buncheez.pk.kitchenapp.model.notificationModel.NotificationResponse;
import buncheez.pk.kitchenapp.model.notificationModel.OrderAcceptResponse;
import buncheez.pk.kitchenapp.model.onlineOrderViewModel.OnlineOrderResponse;
import buncheez.pk.kitchenapp.model.orderListModel.OrderListResponse;
import buncheez.pk.kitchenapp.model.viewOrderModel.ViewOrderResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ChefService {

    @FormUrlEncoded
    @POST("sign_in")
    Call<LoginResponse> doSignIn(@Field("email") String email, @Field("password") String password, @Field("token") String token);

    @FormUrlEncoded
    @POST("orderlist")
    Call<OrderListResponse> getOrderList(@Field("id") String id, @Field("kitchenid") String kitchenid);

    @FormUrlEncoded
    @POST("foodisready")
    Call<FoodReadyResponse> fooReady(@Field("Orderid") String Orderid, @Field("ProductsID") String ProductsID, @Field("variantid") String variantid, @Field("isready") String ready, @Field("kitchenid") String kitchenid);

    @FormUrlEncoded
    @POST("allonlineorder")
    Call<NotificationResponse> allOnlineOrder(@Field("id") String id, @Field("kitchenid") String kitchenid);

    @FormUrlEncoded
    @POST("acceptorder")
    Call<OrderAcceptResponse> acceptOrder(@Field("id") String id, @Field("order_id") String order_id, @Field("kitchenid") String kitchenid, @Field("foodid") String foodid);

    @FormUrlEncoded
    @POST("completeorder")
    Call<CompleteCancelResponse> getCompleteOrder(@Field("id") String id, @Field("start") int start, @Field("kitchenid") String kitchenid);

    @FormUrlEncoded
    @POST("completeorcancel")
    Call<ViewOrderResponse> viewOrder(@Field("Orderid") String Orderid, @Field("kitchenid") String kitchenid);

    @FormUrlEncoded
    @POST("kitchenlist")
    Call<LoginResponse> kitchenlist(@Field("id") String id);

    @FormUrlEncoded
    @POST("viewonlineorder")
    Call<OnlineOrderResponse> viewonlineorder(@Field("id") String id, @Field("order_id") String order_id, @Field("kitchenid") String kitchenid);

    @FormUrlEncoded
    @POST("cancelorder")
    Call<OrderAcceptResponse> cancelorder(@Field("order_id") String order_id, @Field("cancelreason") String cancelreason,@Field("itemId") String itemId);

    @FormUrlEncoded
    @POST("markasready")
    Call<OrderAcceptResponse> markasready(@Field("orderid") String orderid, @Field("foodid") String foodid);
}
