package com.example.happyfishing.entity;

public class FishpitSumaryEntity {

	public String name;
	public String headImageURL;
	public String fishpitSumary;
	public String id;
	public FishpitSumaryEntity(String name, String headImageURL, String fishpitSumary,String id) {
		super();
		this.name = name;
		this.headImageURL = headImageURL;
		this.fishpitSumary = fishpitSumary;
		this.id=id;
	}
	
	
}
