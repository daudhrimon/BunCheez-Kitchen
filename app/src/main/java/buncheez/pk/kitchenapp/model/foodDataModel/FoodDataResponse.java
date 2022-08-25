package buncheez.pk.kitchenapp.model.foodDataModel;

import com.google.gson.annotations.SerializedName;

public class FoodDataResponse{

	@SerializedName("ProductsID")
	private String productsID;

	@SerializedName("variantid")
	private String variantid;

	public void setProductsID(String productsID){
		this.productsID = productsID;
	}

	public String getProductsID(){
		return productsID;
	}

	public void setVariantid(String variantid){
		this.variantid = variantid;
	}

	public String getVariantid(){
		return variantid;
	}

	@Override
 	public String toString(){
		return 
			"FoodDataResponse{" + 
			"productsID = '" + productsID + '\'' + 
			",variantid = '" + variantid + '\'' + 
			"}";
		}
}