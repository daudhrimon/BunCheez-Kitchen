package buncheez.pk.kitchenapp.model.loginModel;

import com.google.gson.annotations.SerializedName;

public class KitchenlistItem{

	@SerializedName("kitchenname")
	private String kitchenname;

	@SerializedName("kitchenid")
	private String kitchenid;

	public String getKitchenname(){
		return kitchenname;
	}

	public String getKitchenid(){
		return kitchenid;
	}
}