package buncheez.pk.kitchenapp.model.onlineOrderViewModel;

import com.google.gson.annotations.SerializedName;

public class OnlineOrderResponse{

	@SerializedName("status_code")
	private int statusCode;

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public int getStatusCode(){
		return statusCode;
	}

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"OnlineOrderResponse{" + 
			"status_code = '" + statusCode + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}