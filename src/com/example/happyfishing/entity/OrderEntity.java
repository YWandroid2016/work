package com.example.happyfishing.entity;

public class OrderEntity {
	public String id;
	public String merchantId;
	public String reserveTime;
	public String location;
	public String orderId;
	public String name;
	public String dateCreated;
	public String totalFee;
	public String picUrl;
	public String merchantName;
	public OrderEntity(String id, String merchantId, String reserveTime, String location, String orderId, String name, String dateCreated, String totalFee, String picUrl,String merchantName) {
		super();
		this.id = id;
		this.merchantId = merchantId;
		this.reserveTime = reserveTime;
		this.location = location;
		this.orderId = orderId;
		this.name = name;
		this.dateCreated = dateCreated;
		this.totalFee = totalFee;
		this.picUrl = picUrl;
		this.merchantName =merchantName;
	}
}
