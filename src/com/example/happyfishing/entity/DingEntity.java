package com.example.happyfishing.entity;

public class DingEntity {

	private String code;
	private String dateCreated;
	private int id;
	private int location;
	private int merchantId;
	private String merchantName;
	private String name;
	private String picUrl;
	private String reserveTime;
	private String strStatus;
	private String type;
	private String category;
	private int totalFee;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}
	public String getStrStatus() {
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	@Override
	public String toString() {
		return "DingEntity [code=" + code + ", dateCreated=" + dateCreated
				+ ", id=" + id + ", location=" + location + ", merchantId="
				+ merchantId + ", merchantName=" + merchantName + ", name="
				+ name + ", picUrl=" + picUrl + ", reserveTime=" + reserveTime
				+ ", strStatus=" + strStatus + ", type=" + type + ", category="
				+ category + ", totalFee=" + totalFee + "]";
	}
	
	public DingEntity() {
		// TODO Auto-generated constructor stub
	}
	public DingEntity(String code, String dateCreated, int id, int location,
			int merchantId, String merchantName, String name, String picUrl,
			String reserveTime, String strStatus, String type, String category,
			int totalFee) {
		super();
		this.code = code;
		this.dateCreated = dateCreated;
		this.id = id;
		this.location = location;
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.name = name;
		this.picUrl = picUrl;
		this.reserveTime = reserveTime;
		this.strStatus = strStatus;
		this.type = type;
		this.category = category;
		this.totalFee = totalFee;
	}
	
	
	
}
