package com.vijayganduri.cybrilla.rateus.enums;

import java.util.HashMap;

/**
 * 
 * Maintain list of all the service. Add more here if any required in future
 * 
 * @author Vijay Ganduri
 *
 */
public enum ServiceEnum {

	SERVICE_1("Service 1", 1),
	SERVICE_2("Service 2", 2),
	SERVICE_3("Service 3", 3),
	SERVICE_4("Service 4", 4),
	SERVICE_5("Service 5", 5),
	SERVICE_6("Service 6", 6),
	SERVICE_7("Service 7", 7),
	SERVICE_8("Service 8", 8),
	SERVICE_9("Service 9", 9),
	SERVICE_10("Service 10", 10),
	SERVICE_11("Service 11", 11);

	private final int id;
	private final String label;

	private static HashMap<Integer, ServiceEnum> lookup;

	private ServiceEnum(String label, int id){
		this.label = label;
		this.id = id;	
	}

	public String getLabel(){
		return this.label;
	}
	
	public int getId(){
		return this.id;
	}

	static{
		lookup = new HashMap<Integer, ServiceEnum>();	     
		for(ServiceEnum e:values()){
			lookup.put(e.getId(), e);
		}
	}
	
}