package ly.travel.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommentFragment extends Fragment {

	@SuppressWarnings("unused")
	private String[] ratings = {
			"food", 
			"kindness", 
			"punctuality", 
			"mileage_program", 
			"comfort",
			"price_quality"
			};
	
	public CommentFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.comments, container, false);
		
	}
	
}
