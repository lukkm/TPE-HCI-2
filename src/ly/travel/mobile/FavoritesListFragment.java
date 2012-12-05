package ly.travel.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FavoritesListFragment extends Fragment {

	public FavoritesListFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*String[] fromColumns = {"Columna 1", 
                "Columna 2"};
		int[] toViews = {R.id.display_1, R.id.display_2};
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(container.getContext(), 
		        R.layout.other, null, fromColumns, toViews, 0);
		ListView listView = (ListView)container.findViewById(R.id.favorites_list_view);
		listView.setAdapter(adapter);*/
		return inflater.inflate(R.layout.favorites_list, container, false);
		
	}
	
}
