package buncheez.pk.kitchenapp.model.onlineOrderViewModel;

import com.google.gson.annotations.SerializedName;

public class AddonslistItem{

	@SerializedName("aodonsqty")
	private String aodonsqty;

	@SerializedName("aodonsname")
	private String aodonsname;

	public String getAodonsqty(){
		return aodonsqty;
	}

	public String getAodonsname(){
		return aodonsname;
	}

	@Override
 	public String toString(){
		return 
			"AddonslistItem{" + 
			"aodonsqty = '" + aodonsqty + '\'' + 
			",aodonsname = '" + aodonsname + '\'' + 
			"}";
		}
}