package buncheez.pk.kitchenapp.model.onlineOrderViewModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("foodinfo")
	private List<FoodinfoItem> foodinfo;

	public List<FoodinfoItem> getFoodinfo(){
		return foodinfo;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"foodinfo = '" + foodinfo + '\'' + 
			"}";
		}
}