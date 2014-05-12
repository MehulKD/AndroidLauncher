package com.kregelbagel.android.drawer;

import java.util.ArrayList;
import java.util.List;

import com.kregelbagel.android.core.Config;
import com.kregelbagel.android.fragments.AppsDrawerFragment;
import com.kregelbagel.android.fragments.CameraFragment;
import com.kregelbagel.android.fragments.FragmentOne;
import com.kregelbagel.android.fragments.FragmentThree;
import com.kregelbagel.android.fragments.FragmentTwo;
import com.kregelbagel.android.fragments.SettingsFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;
	PackageManager pm;
	List<DrawerItem> dataList;
	Config cnf;

	public Context returnContext() {
		return getApplicationContext();
	}

	GridLayout gv;

	public Config getConfig() {
		return cnf;
	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pm = this.getPackageManager();
		final WallpaperManager wallpaper = WallpaperManager.getInstance(this);
		final Drawable wallpaperDraw = wallpaper.getFastDrawable();
		getWindow().setBackgroundDrawable(wallpaperDraw);
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		cnf = new Config();
		Config.context = getApplicationContext();
		Config.fragmentManager = getFragmentManager();
		setContentView(R.layout.activity_main);

		// Initializing
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// Add Drawer Item to dataList
		dataList.add(new DrawerItem(true)); // adding a spinner to the list

		dataList.add(new DrawerItem("My Favorites")); // adding a header to the list
		dataList.add(new DrawerItem("App drawer", R.drawable.ic_action_settings));
		dataList.add(new DrawerItem("Games", R.drawable.ic_action_gamepad));
		dataList.add(new DrawerItem("Lables", R.drawable.ic_action_labels));

		dataList.add(new DrawerItem("Main Options"));// adding a header to the list
		dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
		dataList.add(new DrawerItem("Camara", R.drawable.ic_action_camera));
		dataList.add(new DrawerItem("Video", R.drawable.ic_action_video));

		dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
		dataList.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
		dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));
		dataList.add(new DrawerItem("Messages", R.drawable.ic_action_camera));
		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
		    R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {

			if (dataList.get(0).isSpinner() & dataList.get(1).getTitle() != null) {
				SelectItem(2);
			} else if (dataList.get(0).getTitle() != null) {
				SelectItem(1);
			} else {
				SelectItem(0);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SelectItem(int possition) {

		Fragment fragment = null;
		Bundle args = new Bundle();
		switch (possition) {

		case 2:
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			/*
			 * Add more functionality to the app for AppWidgets.
			 * http://developer.android.com/guide/topics/appwidgets/host.html
			 * http://developer
			 * .android.com/reference/android/appwidget/AppWidgetHost.html
			 * 
			 * for sms Messaging
			 * https://web.archive.org/web/20121022021217/http://mobdev
			 * .olin.edu/mobdevwiki/FrontPage/Tutorials/SMS%20Messaging
			 * https://code.google
			 * .com/p/android-smspopup/source/browse/SMSPopup/src/net
			 * /everythingandroid/smspopup/util/SmsPopupUtils.java?r=
			 * f5505a976d846531a1c3294975d33c0694259397
			 * 
			 * reading sms
			 * https://code.google.com/p/android-smspopup/source/browse/SMSPopup
			 * /src/net/everythingandroid/smspopup/util/SmsPopupUtils.java?r=
			 * f5505a976d846531a1c3294975d33c0694259397
			 */
			break;
		case 3:
			fragment = new AppsDrawerFragment();

			break;
		case 4:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
		case 5:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
		case 6:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());

			break;
		case 7:
			fragment = new CameraFragment();
			break;
		case 8:
			fragment = new FragmentThree();
			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
		case 9:
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
		case 10:
			fragment = new SettingsFragment();
			break;
		case 11:
			fragment = new FragmentThree();
			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;

		case 12:
			/*
			 * Textmessaging for android shit :D
			 * http://mobiforge.com/design-development/sms-messaging-android
			 */
			fragment = new FragmentThree();
			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;

		default:
			break;
		}
		FragmentManager frgManager = getFragmentManager();

		fragment.setArguments(args);
		frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		mDrawerList.setItemChecked(possition, true);
		setTitle(dataList.get(possition).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return false;
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (dataList.get(position).getTitle() == null) {
				SelectItem(position);
			}

		}
	}

}
