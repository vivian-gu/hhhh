package soosokan.Entity;

import java.util.List;

public class ShopList {

	private String sellerId;
	private String name;
	private String address;
	private double distance;
	private List<String> result;
	
	
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(Float distance) {
		this.distance = distance;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public ShopList(String sellerId, String name, double distance, String address, List<String> result) {
		super();
		this.sellerId = sellerId;
		this.name = name;
		this.distance = distance;
		this.address = address;
		this.result = result;
	}
	public ShopList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<String> getResult() {
		return result;
	}
	public void setResult(List<String> result) {
		this.result = result;
	}
}	
