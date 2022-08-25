package buncheez.pk.kitchenapp.model.loginModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("firstname")
	private String firstname;

	@SerializedName("PowerBy")
	private String powerBy;

	@SerializedName("UserPictureURL")
	private String userPictureURL;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("picture")
	private String picture;

	@SerializedName("lastname")
	private String lastname;

	@SerializedName("currencysign")
	private String currencysign;
	@SerializedName("kitchenid")
	private String kitchenid;
	@SerializedName("kitchenlist")
	private List<KitchenlistItem> kitchenlist;

	public List<KitchenlistItem> getKitchenlist(){
		return kitchenlist;
	}
	public String getCurrencysign() {
		return currencysign;
	}

	public String getKitchenid() {
		return kitchenid;
	}

	public void setCurrencysign(String currencysign) {
		this.currencysign = currencysign;
	}

	public void setFirstname(String firstname){
		this.firstname = firstname;
	}

	public String getFirstname(){
		return firstname;
	}

	public void setPowerBy(String powerBy){
		this.powerBy = powerBy;
	}

	public String getPowerBy(){
		return powerBy;
	}

	public void setUserPictureURL(String userPictureURL){
		this.userPictureURL = userPictureURL;
	}

	public String getUserPictureURL(){
		return userPictureURL;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	public String getLastname(){
		return lastname;
	}

	@Override
	public String toString(){
		return
				"Data{" +
						"firstname = '" + firstname + '\'' +
						",powerBy = '" + powerBy + '\'' +
						",userPictureURL = '" + userPictureURL + '\'' +
						",id = '" + id + '\'' +
						",email = '" + email + '\'' +
						",picture = '" + picture + '\'' +
						",lastname = '" + lastname + '\'' +
						"}";
	}
}