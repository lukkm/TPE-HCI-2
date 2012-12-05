package ly.travel.mobile.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Airline {

	private String id, name, logoUrl;
	
	public Airline(String id, String name, String logoUrl){
		this.id = id;
		this.name = name;
		this.logoUrl = logoUrl;
	}
	
	public Airline(JSONObject airline) throws JSONException {
		this.id = airline.getString("airlineId");
		this.name = airline.getString("name");
		this.logoUrl = airline.getString("logo");
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLogoUrl() {
		return this.logoUrl;
	}
	
}
