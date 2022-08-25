package buncheez.pk.kitchenapp.model.onlineOrderViewModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FoodinfoItem{

	@SerializedName("addons")
	private int addons;

	@SerializedName("addonslist")
	private List<AddonslistItem> addonslist;

	@SerializedName("qty")
	private String qty;

	@SerializedName("FoodID")
	private String foodID;

	@SerializedName("OrderID")
	private String orderID;

	@SerializedName("FoodName")
	private String foodName;

	public int getAddons(){
		return addons;
	}

	public List<AddonslistItem> getAddonslist(){
		return addonslist;
	}

	public String getQty(){
		return qty;
	}

	public String getFoodID(){
		return foodID;
	}

	public String getOrderID(){
		return orderID;
	}

	public String getFoodName(){
		return foodName;
	}

	@Override
 	public String toString(){
		return 
			"FoodinfoItem{" + 
			"addons = '" + addons + '\'' + 
			",addonslist = '" + addonslist + '\'' + 
			",qty = '" + qty + '\'' + 
			",foodID = '" + foodID + '\'' + 
			",orderID = '" + orderID + '\'' + 
			",foodName = '" + foodName + '\'' + 
			"}";
		}
}