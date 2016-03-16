package com.example.happyfishing.entity;

public class FishpitDetailEntity {

	public String []ADBannerimageUrls;
	public int fishPositionTotal;
	public String envirScore;
	public boolean haveWIFI;
	public boolean haveParking;
	public String location;
	//纬度
	public double latitude;
	//经度
	public double longitude;
	public String fishipitDetail;
	public FishpitDetailEntity(String[] ADBannerimageUrls, int fishPositionTotal, String envirScore, boolean haveWIFI, boolean haveParking, String location, double latitude,double longitude, String fishipitDetail) {
		super();
		this.ADBannerimageUrls = ADBannerimageUrls;
		this.fishPositionTotal = fishPositionTotal;
		this.envirScore = envirScore;
		this.haveWIFI = haveWIFI;
		this.haveParking = haveParking;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fishipitDetail = fishipitDetail;
	}
}
