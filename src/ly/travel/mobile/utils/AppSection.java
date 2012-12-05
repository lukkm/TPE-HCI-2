package ly.travel.mobile.utils;

import android.support.v4.app.Fragment;

public class AppSection {

	private int title_string;
	private int resId;
	private Fragment fragment;
	
	public AppSection (int title, Fragment fragment, int resId) {
		this.title_string = title;
		this.fragment = fragment;
		this.resId = resId;
	}
	
	public Fragment getFragment() {
		return this.fragment;
	}
	
	public int getTitleString() {
		return this.title_string;
	}
	
	public int getResId() {
		return this.resId;
	}
	
}
