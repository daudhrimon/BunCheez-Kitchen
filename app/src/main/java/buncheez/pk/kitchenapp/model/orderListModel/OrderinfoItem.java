package buncheez.pk.kitchenapp.model.orderListModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderinfoItem{

	@SerializedName("TableName")
	private String tableName;

	@SerializedName("iteminfo")
	private List<IteminfoItem> iteminfo;

	@SerializedName("CustomerName")
	private String customerName;

	@SerializedName("TotalAmount")
	private String totalAmount;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("OrderDate")
	private String orderDate;
	@SerializedName("notes")
	private String notes;


	public String getNotes() {
		return notes;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public String getTableName(){
		return tableName;
	}

	public void setIteminfo(List<IteminfoItem> iteminfo){
		this.iteminfo = iteminfo;
	}

	public List<IteminfoItem> getIteminfo(){
		return iteminfo;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return customerName;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	@Override
 	public String toString(){
		return 
			"OrderinfoItem{" + 
			"tableName = '" + tableName + '\'' + 
			",iteminfo = '" + iteminfo + '\'' + 
			",customerName = '" + customerName + '\'' + 
			",totalAmount = '" + totalAmount + '\'' + 
			",order_id = '" + orderId + '\'' + 
			",orderDate = '" + orderDate + '\'' + 
			"}";
		}
}