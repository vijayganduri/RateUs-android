package com.vijayganduri.cybrilla.rateus.beans;

import java.io.Serializable;

public class Rating implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7305747137346642126L;
	
	private String serviceType;	
	private int rating;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	@Override
	public String toString() {
		return "Rating [serviceType=" + serviceType + ", rating=" + rating
				+ "]";
	}
	
}