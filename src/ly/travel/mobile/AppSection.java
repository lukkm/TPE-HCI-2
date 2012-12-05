package ly.travel.mobile;

import android.support.v4.app.Fragment;

public class AppSection {

	private int title_string;
	private Fragment fragment;
	
	public AppSection (int title, Fragment fragment) {
		this.title_string = title;
		this.fragment = fragment;
	}
	
	public Fragment getFragment() {
		return this.fragment;
	}
	
	public int getTitleString() {
		return this.title_string;
	}
	
}
