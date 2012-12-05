package ly.travel.mobile;

import ly.travel.mobile.Fragments.CommentFragment;
import ly.travel.mobile.Fragments.FavoriteInfoFragment;
import ly.travel.mobile.Fragments.FavoritesFragment;
import ly.travel.mobile.Fragments.MainFragment;
import ly.travel.mobile.Fragments.SettingsFragment;
import ly.travel.mobile.utils.ActionHandler;
import ly.travel.mobile.utils.AppSection;
import ly.travel.mobile.utils.Favorite;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private Fragment commentFragment = new CommentFragment();
	private Fragment favoritesFragment = new FavoritesFragment();
	private Fragment mainFragment = new MainFragment();
	
	private Intent notificationService;
	
	private AppSection[] sections = {
			new AppSection(R.string.comments_title, commentFragment, R.drawable.ic_action_comment),
			new AppSection(R.string.favorites_title, favoritesFragment, R.drawable.ic_action_favorite) };

	private Fragment currentFragment;

	@Override
	protected void onDestroy() {
		stopService(notificationService);
		super.onDestroy();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		NotificationsService.activity = this;
		NotificationsService.secondsToSleep = 30;
		
		notificationService = new Intent(Intent.ACTION_SYNC, null, this, NotificationsService.class);
		startService(notificationService);

		/* Set up the action bar. */
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		/* For each of the sections in the app, add a tab to the action bar. */
		
		for (int i = 0; i < sections.length; i++) {
			actionBar
					.addTab(actionBar.newTab()
							.setText(sections[i].getTitleString())
							.setIcon(sections[i].getResId())
							.setTabListener(this));
			
		}
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.selectTab(null);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, mainFragment).commit();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		/*
		 * When the given tab is selected, show the tab fragment in the
		 * container
		 */

		int tabPosition = tab.getPosition();
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, sections[tabPosition].getFragment())
				.addToBackStack(null)
				.commit();
		
		currentFragment = sections[tabPosition].getFragment();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				getSupportFragmentManager().popBackStack();
				getSupportFragmentManager().executePendingTransactions();
				getActionBar().selectTab(null);
				break;
			case R.id.menu_settings:
				getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new SettingsFragment())
				.addToBackStack(null)
				.commit();
			break;
		}
		return true;
	}
	
	public void goToNewFavoriteInfoFragment(Favorite fav){
		getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new FavoriteInfoFragment(fav))
					.addToBackStack(null)
					.commit();
	}
	
	public void goToNewFavoriteInfoFragmentLarge(Favorite fav){
		getSupportFragmentManager().beginTransaction()
					.replace(R.id.info_container, new FavoriteInfoFragment(fav))
					.addToBackStack(null)
					.commit();
	}

	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public void addFavorite(View view) {
		ActionHandler current = (ActionHandler) currentFragment;
		current.Handle(FunctionsList.ADD_FAVORITE, view);
	}
	
	public void removeFavorite(View view) {
		ActionHandler current = (ActionHandler) currentFragment;
		current.Handle(FunctionsList.REMOVE_FAVORITE, view);
		changeToFavoriteFragment();
	}
	
	public void acceptChangesOnPreferences(View view) {
		changeToFavoriteFragment();
	}
	
	private void changeToFavoriteFragment() {
		int screenLayout = getResources().getConfiguration().screenLayout;
		if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.info_container, new Fragment())
				.commit();
		} else {
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, favoritesFragment)
			.addToBackStack(null)
			.commit();
		}
	}
	
	public void sendComment(View view){
		ActionHandler current = (ActionHandler) currentFragment;
		current.Handle(FunctionsList.SEND_COMMENT, view);
	}
	
	public Fragment getMainFragment(){
		return mainFragment;
	}

}
