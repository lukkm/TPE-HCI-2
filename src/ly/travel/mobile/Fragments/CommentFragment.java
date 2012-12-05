package ly.travel.mobile.Fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ly.travel.mobile.FlightsAPIService;
import ly.travel.mobile.FunctionsList;
import ly.travel.mobile.MainActivity;
import ly.travel.mobile.QueryIntent;
import ly.travel.mobile.R;
import ly.travel.mobile.RequestReceiver;
import ly.travel.mobile.utils.ActionHandler;
import ly.travel.mobile.utils.Airline;
import ly.travel.mobile.utils.Comment;

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
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CommentFragment extends Fragment implements ActionHandler {
	
	private Map<String, Airline> airlinesMap;
	
	public CommentFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.comments, container, false);
		QueryIntent query = new QueryIntent(new RequestReceiver() {

			@Override
			public void onStarted() {
			}

			@Override
			public void onData(String data) {
				if (getActivity() == null)
					return;
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
		
		return view;
		
	}

	public void Handle(FunctionsList function, View view) {
		switch(function) {
		case SEND_COMMENT:
			sendComment(view);
			break;
		}
		return;
	}
	
	private void sendComment(View view) {
		
		String airlineName = getStringFromField(R.id.airline_auto_complete);
		
		if (airlinesMap == null){
			Toast.makeText(getActivity(), getActivity().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
			return;
		}
		
		Airline airline = airlinesMap.get(airlineName);
		
		String flightNumber = getStringFromField(R.id.flightNumber);
		
		if (airline == null) {
			Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_airline), Toast.LENGTH_SHORT).show();
		} else if (flightNumber.equals("")) {
			Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_flight_number), Toast.LENGTH_SHORT).show();
		} else {
			
			Comment comment = new Comment(
					airline.getId(),
					Integer.valueOf(flightNumber),
					getRatingFrom(R.id.food),
					getRatingFrom(R.id.kindness),
					getRatingFrom(R.id.punctuality),
					getRatingFrom(R.id.mileage_program),
					getRatingFrom(R.id.comfort),
					getRatingFrom(R.id.price_quality),
					getValueFromSwitch(R.id.yes_no_recommend),
					getStringFromField(R.id.comments_text)
					);
			
			Map<String,String> map = new HashMap<String,String>();	
			map.put("data", comment.getParameters());
			
			QueryIntent query = new QueryIntent(new RequestReceiver() {
				
				@Override
				public void onStarted() {
				}
				
				@Override
				public void onData(String data) {
					try {
						JSONObject json = new JSONObject(data);
						if (json.optBoolean("review")){
							Toast.makeText(getActivity(), getActivity().getString(R.string.successfull_comment), Toast.LENGTH_SHORT).show();
							getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.container, ((MainActivity)getActivity()).getMainFragment())
							.addToBackStack(null)
							.commit();
						} else {
							Toast.makeText(getActivity(), getActivity().getString(R.string.failed_to_comment), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onError(String error) {
				}
				
			}, FlightsAPIService.GET, "Review", "ReviewAirline2", map, "", getActivity());
			
			getActivity().startService(query);
		}
		
	}
	
	private String getStringFromField(int fieldId){
		return ((TextView)getActivity().findViewById(fieldId)).getText().toString();
	}
	
	private int getRatingFrom(int ratingBarId) {
		RatingBar rb = (RatingBar) getActivity().findViewById(ratingBarId);
		return Math.round((rb.getRating() * 8 / 5)) + 1;
	}
	
	private boolean getValueFromSwitch(int switchId) {
		Switch sw = (Switch) getActivity().findViewById(switchId);
		return sw.isChecked();
	}
	
}
