package buncheez.pk.kitchenapp.model.notificationModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("orderinfo")
	private List<OrderinfoItem> orderinfo;

	public void setOrderinfo(List<OrderinfoItem> orderinfo){
		this.orderinfo = orderinfo;
	}

	public List<OrderinfoItem> getOrderinfo(){
		return orderinfo;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"orderinfo = '" + orderinfo + '\'' + 
			"}";
		}
}