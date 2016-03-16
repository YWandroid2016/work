package com.example.happyfishing.entity;

public class BillEntity {
	public int jifenType;
	public String pointUse ;
	public String type ;
	public String pointFrom;
	public String date;
	public String name;
	public BillEntity(int jifenType,String pointUse, String type, String pointFrom, String date, String name) {
		super();
		this.jifenType = jifenType;
		this.pointUse = pointUse;
		this.type = type;
		this.pointFrom = pointFrom;
		this.date = date;
		this.name = name;
	}
	
	
}
