package ly.travel.mobile.Fragments;

import ly.travel.mobile.R;
import ly.travel.mobile.utils.Favorite;
import ly.travel.mobile.utils.NotificationConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class FavoriteInfoFragment extends Fragment {

	private Favorite currentFavorite;
	private View view;

	private final String preferencesFileName = "favoritesPreferencesStorage";

	public FavoriteInfoFragment(Favorite fav) {
		currentFavorite = fav;
	}

	public FavoriteInfoFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.favorite_information, container, false);
		setIntoTextView(R.id.flight_number,
				getActivity().getString(R.string.flight_number) + " "
						+ currentFavorite.getFlightNumber());
		setIntoTextView(R.id.departure_city, getCityFromString(currentFavorite
				.getDeparture().getCityName()));
		setIntoTextView(R.id.arrival_city, getCityFromString(currentFavorite
				.getArrival().getCityName()));
		setIntoTextView(R.id.airline_name, currentFavorite.getAirline()
				.getName());
		setIntoTextView(R.id.status,
				getStatus(currentFavorite.getFlightStatus()));
		setIntoTextView(R.id.airport_terminal, currentFavorite.getDeparture()
				.getAirportTerminal());
		setIntoTextView(R.id.airport_gate, currentFavorite.getDeparture()
				.getAirportGate());
		setIntoTextView(R.id.scheduled_time, currentFavorite.getDeparture()
				.getScheduledTime());
		setChecked(R.id.status_check, currentFavorite.getConfig()
				.isNotifyOnStatusChanged());
		setChecked(R.id.time_check, currentFavorite.getConfig()
				.isNotifyOnScheduledTimeChanged());
		setChecked(R.id.terminal_check, currentFavorite.getConfig()
				.isNotifyOnTerminalChanged());
		setChecked(R.id.gate_check, currentFavorite.getConfig()
				.isNotifyOnGateChanged());
		return view;
	}

	private void setIntoTextView(int textViewId, String string) {
		TextView textView = (TextView) view.findViewById(textViewId);
		textView.setText(string);
	}

	private String getStatus(String status) {
		Log.d("Flight status", status);
		if (status.equals("S"))
			return getActivity().getString(R.string.scheduled);
		else if (status.equals("A"))
			return getActivity().getString(R.string.active);
		else if (status.equals("D"))
			return getActivity().getString(R.string.deviated);
		else if (status.equals("L"))
			return getActivity().getString(R.string.landed);
		else if (status.equals("C"))
			return getActivity().getString(R.string.cancelled);
		return getActivity().getString(R.string.unknown);

	}

	private String getCityFromString(String s) {
		return s.substring(0, s.indexOf(','));
	}

	@Override
	public void onDestroyView() {
		Log.w("PREFS", "DESTROYED");
		SharedPreferences prefs = getActivity().getSharedPreferences(
				preferencesFileName, Context.MODE_WORLD_WRITEABLE);
		Editor editor = prefs.edit();
		String uniqueKey = currentFavorite.getKey();
		
		NotificationConfiguration conf = currentFavorite.getConfig();
		conf.setNotifyOnStatusChanged(getChecked(R.id.status_check));
		conf.setNotifyOnGateChanged(getChecked(R.id.gate_check));
		conf.setNotifyOnTerminalChanged(getChecked(R.id.terminal_check));
		conf.setNotifyOnScheduledTimeChanged(getChecked(R.id.time_check));
	
		JSONObject jsonPrefs;
		
		try {
			jsonPrefs = currentFavorite.generateJsonPreferences();
			editor.putString(uniqueKey, jsonPrefs.toString()).commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		super.onDestroyView();
	}

	private boolean getChecked(int checkboxId) {
		CheckBox cb = (CheckBox) view.findViewById(checkboxId);
		return cb.isChecked();
	}

	private void setChecked(int checkboxId, boolean checked) {
		CheckBox cb = (CheckBox) view.findViewById(checkboxId);
		cb.setChecked(checked);
	}

}
