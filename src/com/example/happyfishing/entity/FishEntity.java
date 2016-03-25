package com.example.happyfishing.entity;

import java.util.List;

public class FishEntity {

	private int status;
	private List<DingEntity> orders;
	private String text;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<DingEntity> getOrders() {
		return orders;
	}
	public void setOrders(List<DingEntity> orders) {
		this.orders = orders;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "FishEntity [status=" + status + ", orders=" + orders
				+ ", text=" + text + "]";
	}
	
	public FishEntity() {
		// TODO Auto-generated constructor stub
	}
	public FishEntity(int status, List<DingEntity> orders, String text) {
		super();
		this.status = status;
		this.orders = orders;
		this.text = text;
	}
	
}
