package buncheez.pk.kitchenapp.model.orderListModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("orderinfo")
	private List<OrderinfoItem> orderinfo;
	@SerializedName("hasitem")
	private int hasitem;

	public int getHasitem() {
		return hasitem;
	}
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