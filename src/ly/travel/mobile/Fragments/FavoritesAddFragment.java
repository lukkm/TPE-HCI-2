package ly.travel.mobile.Fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ly.travel.mobile.FlightsAPIService;
import ly.travel.mobile.QueryIntent;
import ly.travel.mobile.R;
import ly.travel.mobile.RequestReceiver;
import ly.travel.mobile.utils.Airline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class FavoritesAddFragment extends Fragment {

	private View view;
	private Map<String, Airline> airlinesMap;

	public FavoritesAddFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.favorites_add, container, false);
			QueryIntent query = new QueryIntent(new RequestReceiver() {

				@Override
				public void onStarted() {
				}

				@Override
				public void onData(String data) {
					if (getActivity().findViewById(R.id.airline_auto_complete) == null)
						return;
					Airline airline;
					airlinesMap = new HashMap<String,Airline>();
					List<String> lstAirlines = new ArrayList<String>();
					JSONObject jo;
					try {
						jo = new JSONObject(data);
						JSONArray joAirlines = jo.getJSONArray("airlines");
						for (int i = 0; i < joAirlines.length(); i++) {
							airline = new Airline(joAirlines.getJSONObject(i));
							lstAirlines.add(airline.getName());
							airlinesMap.put(airline.getName(), airline);
						}

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_dropdown_item_1line,
								lstAirlines);

						AutoCompleteTextView textView = (AutoCompleteTextView) getActivity()
								.findViewById(R.id.airline_auto_complete);
						
						textView.setAdapter(adapter);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onError(String error) {
				}

			}, FlightsAPIService.GET, "Misc", "GetAirlines", new HashMap<String,String>(), "", getActivity());
			
			getActivity().startService(query);
		}
		return view;
	}

	@Override
	public void onDestroyView() {
		((ViewGroup) view.getParent()).removeAllViews();
		super.onDestroyView();
	}
	
	public Map<String, Airline> getAirlinesMap(){
		return airlinesMap;
	}

}
