package com.vijayganduri.cybrilla.rateus.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.vijayganduri.cybrilla.rateus.dao.VoteDao;

@DatabaseTable(tableName = "vote", daoClass = VoteDao.class)
public class Vote{

	@DatabaseField(unique = true, generatedId = true)
	private long id;

	@DatabaseField
	private long userid;

	@DatabaseField
	private String serviceType;
	
	@DatabaseField
	private int rating;
	
	@DatabaseField
	private String additionalInfo;

	@DatabaseField
	private long updatedAt;
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

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

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Vote [id=" + id + ", userid=" + userid + ", serviceType="
				+ serviceType + ", rating=" + rating + ", additionalInfo="
				+ additionalInfo + ", updatedAt=" + updatedAt + "]";
	}

}