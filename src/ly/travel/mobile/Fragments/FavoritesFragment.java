package ly.travel.mobile.Fragments;

import ly.travel.mobile.FunctionsList;
import ly.travel.mobile.R;
import ly.travel.mobile.utils.ActionHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FavoritesFragment extends Fragment implements ActionHandler{

	View view;
	ViewGroup vg;
	Fragment addFragment;
	Fragment listFragment;
	
	public FavoritesFragment(){
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			vg = container;
			view = inflater.inflate(R.layout.favorites, container, false);
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			addFragment = new FavoritesAddFragment();
			listFragment = new FavoritesListFragment();
			
			ft.add(R.id.favorites_add_fragment_container, addFragment);
			ft.add(R.id.favorites_list_fragment_container, listFragment);
			ft.commit();
			
		}
		ViewGroup parent = (ViewGroup)vg.getParent();
		parent.removeView(view);
		container.removeView(view);
		return view;
	}

	@Override
	public void onDestroyView() {
		((ViewGroup)view.getParent()).removeAllViews();
		super.onDestroyView();
	}


	public void Handle(FunctionsList function, View view) {
		switch (function) {
		case ADD_FAVORITE:
			((FavoritesListFragment)listFragment).setAirlinesMap(((FavoritesAddFragment)addFragment).getAirlinesMap());
			((ActionHandler)listFragment).Handle(FunctionsList.ADD_FAVORITE, view);
			break;
		case REMOVE_FAVORITE:
			((ActionHandler)listFragment).Handle(FunctionsList.REMOVE_FAVORITE, view);
			break;
		}
		return;		
	} 
	
	
	
	
}
