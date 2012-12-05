package ly.travel.mobile.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Comment {

	private int flightNumber, foodRating, kindnessRating, punctualityRating,
			mileageRating, comfortRating, priceRating;
	private String airlineId, comments;
	private boolean yesRecommend;

	public Comment(String id, int number, int food, int kindness,
			int punctuality, int mileage, int comfort, int price,
			boolean recommend, String comments) {
		this.airlineId = id;
		this.flightNumber = number;
		this.foodRating = food;
		this.kindnessRating = kindness;
		this.punctualityRating = punctuality;
		this.mileageRating = mileage;
		this.comfortRating = comfort;
		this.priceRating = price;
		this.yesRecommend = recommend;
		this.comments = comments;
	}

	public String getParameters() {				
		JSONObject ans = new JSONObject();
		try {
			ans.put("airlineId", airlineId);
			ans.put("flightNumber", flightNumber);
			ans.put("friendlinessRating", kindnessRating);
			ans.put("foodRating", foodRating);
			ans.put("punctualityRating", punctualityRating);
			ans.put("mileageProgramRating", mileageRating);
			ans.put("comfortRating", comfortRating);
			ans.put("qualityPriceRating", priceRating);
			ans.put("yesRecommend", yesRecommend);
			ans.put("comments", comments);
		} catch (JSONException e) {
			Log.wtf("wtf", "JSONException");
		}
		
		return ans.toString();
		
	}

}
