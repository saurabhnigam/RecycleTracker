package com.green.beans;

public class QueryResultBean {
	
	String itemName ,quantity ,latitude , longitude ,task;
	int taskid,userid;

	//userinfo table's fields
	public static final String KEY_USERID ="userid";
	public static final String KEY_EMAILID ="emailid";
	public static final String KEY_PASS ="password";
	
	
	public QueryResultBean() {
	}


	public QueryResultBean(String itemName, String quantity, String latitude,
			String longitude, String task, int taskid, int userid) {
		super();
		this.itemName = itemName;
		this.quantity = quantity;
		this.latitude = latitude;
		this.longitude = longitude;
		this.task = task;
		this.taskid = taskid;
		this.userid = userid;
	}


	public String getItemName() {
		return itemName;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getTask() {
		return task;
	}

	public int getTaskid() {
		return taskid;
	}

	public int getUserid() {
		return userid;
	}
	
}
