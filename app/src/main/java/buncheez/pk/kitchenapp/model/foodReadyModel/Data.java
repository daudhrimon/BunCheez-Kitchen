package buncheez.pk.kitchenapp.model.foodReadyModel;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("isready")
	private String isready;

	public void setIsready(String isready){
		this.isready = isready;
	}

	public String getIsready(){
		return isready;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"isready = '" + isready + '\'' + 
			"}";
		}
}