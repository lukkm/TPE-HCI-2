package ly.travel.mobile;

import java.util.ArrayList;
import java.util.Map;

import ly.travel.mobile.Fragments.FavoritesListFragment;
import ly.travel.mobile.utils.Favorite;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import apiClasses.ApiIntent;
import apiClasses.ApiResultReceiver;
import apiClasses.ApiService;
import apiClasses.CallBack;


public class NotificationsService extends IntentService {

	private final String fileName = "favoritesStorage";
	private final String preferencesFileName = "favoritesPreferencesStorage";

	public static Activity activity;
	public static long secondsToSleep;
	
	private ApiResultReceiver receiver = new ApiResultReceiver(new Handler()) {

		@Override
    	protected void onReceiveResult(int resultCode, Bundle resultData) {
    		super.onReceiveResult(resultCode, resultData);
    		if (resultCode == ApiService.STATUS_OK) {

    			String responseString = (String) resultData
    					.getSerializable("return");
    			JSONObject response = new JSONObject();
				try {
					response = new JSONObject(responseString);
				} catch (JSONException e) {
					Log.d("JSON", "Todo mal");
				}
    			callback.handleResponse(response);
    			
    		} else if (resultCode == ApiService.STATUS_CONNECTION_ERROR) {
    			Log.d("Api Service", "Connection error.");
    		} else {
    			Log.d("Api Service", "Unknown error.");
    		}
    	}
	};
	
	public NotificationsService() {
		super("NotificationsService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while(true) {
			
				try {
					synchronized (this) {
						wait(secondsToSleep * 1000);
						try {
							retrieveData();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						for (Favorite f: FavoritesListFragment.favList) {
							
							Log.w("Debugging Flight", String.valueOf(f.getFlightNumber()));
							
							
							synchronized(this) {
								checkStatus(f);
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}		
	
	
	private void checkStatus(final Favorite f) {

		CallBack callback = new CallBack() {

			public void handleResponse(JSONObject jo) {
				if (jo.has("error")) {
					return;
				}
				Favorite flight;
				try {
					JSONObject joStatus = jo.getJSONObject("status");
					flight = new Favorite(joStatus);
					String s = flight.getNotificationString(f);
					if (!s.equals("")) {
						NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationsService.this);
						builder.setContentTitle(activity.getString(R.string.notification_title) + String.valueOf(flight.getFlightNumber()));
						builder.setContentText(s);
						builder.setSmallIcon(R.drawable.ic_action_deals);
						
						NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
						manager.notify(0, builder.build());
					} else {
						Log.d("Vuelo se mantuvo igual", "Vuelo " + flight.getFlightNumber());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		receiver.setCallBack(callback);
		ApiIntent intent = new ApiIntent("GetFlightStatus", "Status",
				this.receiver, this);
		intent.setParams(f.getParams());
		startService(intent);
		synchronize(this);
	}
	
	private void retrieveData() throws JSONException {
		FavoritesListFragment.favList = new ArrayList<Favorite>();
		SharedPreferences prefs = activity.getSharedPreferences(fileName, Context.MODE_WORLD_READABLE);
		Map<String, String> map = (Map<String,String>)prefs.getAll();
		for (String s: map.values()){
			FavoritesListFragment.favList.add(new Favorite(new JSONObject(s)));
		}
		prefs = activity.getSharedPreferences(preferencesFileName, Context.MODE_WORLD_READABLE);
		map = (Map<String,String>)prefs.getAll();
		Favorite fav;
		JSONObject jsonPrefs;
		for (String s: map.keySet()){
			fav = getFavorite(s);
			jsonPrefs = new JSONObject(map.get(s));
			if (fav != null) {
				fav.setNotificationConfiguration(jsonPrefs);
			}
		}
	}
	
	private Favorite getFavorite(String key) {
		for(Favorite f : FavoritesListFragment.favList) {
			if (f.getKey().equals(key))
				return f;
		}
		return null;
	}
	
	private void synchronize(Context context){
		try {
			wait(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
