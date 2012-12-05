package ly.travel.mobile.Fragments;

import ly.travel.mobile.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DealsFragment extends Fragment {

	public DealsFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.deals, container, false);
		
	}
	
}
