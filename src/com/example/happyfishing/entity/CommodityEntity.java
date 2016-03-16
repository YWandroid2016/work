package com.example.happyfishing.entity;

public class CommodityEntity {

	public String idString;
	public String imageURL;
	public String commoditySumary;
	public String price;
	public String sellNum;
	public CommodityEntity(String idString,String imageURL, String commoditySumary, String price, String sellNum) {
		super();
		this.imageURL = imageURL;
		this.commoditySumary = commoditySumary;
		this.price = price;
		this.sellNum = sellNum;
		this.idString = idString;
	}
	
}
