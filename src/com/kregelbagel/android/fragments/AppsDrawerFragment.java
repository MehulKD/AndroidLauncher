package com.kregelbagel.android.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.kregelbagel.android.core.Config;
import com.kregelbagel.android.core.Apps;
import com.kregelbagel.android.drawer.R;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AppsDrawerFragment extends Fragment {
	ImageView imgview;
	PackageManager pm;
	public ArrayList<ApplicationInfo> data = new ArrayList<ApplicationInfo>(42);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.drawer2, container, false);
		if (pm == null) {
			pm = Config.context.getPackageManager();
		}
		TableLayout gv = (TableLayout) view.findViewById(R.id.gridView1);
		gv.setWeightSum(2f);
		Config.context.getResources().getDisplayMetrics();
		int i = 0;
		List<Apps> loadedApps = listAllw2();
		Collections.sort(loadedApps, new Comparator<Apps>() {

			@Override
			public int compare(Apps lhs, Apps rhs) {
				return lhs.getTitle().compareTo(rhs.getTitle());
			}
		});
		// Config.makeColdToast(loadedApps.size());

		for (final Apps a : loadedApps) {
			final TableRow tb = new TableRow(Config.context);
			tb.setId(i + 1000);
			Drawable ico = null;
			try {
				Intent in = pm.getLaunchIntentForPackage(a.getPackageName());
				if (in != null) {
					ico = pm.getActivityIcon(in);

				}
			} catch (NameNotFoundException e) {
			}
			ImageView ima = new ImageView(Config.context);
			ima.setId(i + 500);
			ima.setImageDrawable(ico);
			ima.setLayoutParams(new android.widget.TableLayout.LayoutParams(Config.dpToPx(50), Config.dpToPx(50), 0.2f));

			tb.addView(ima, new TableRow.LayoutParams(Config.dpToPx(50), Config.dpToPx(50)));
			TextView name = new TextView(Config.context);
			name.setId(i);
			name.setLayoutParams(new android.widget.TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
			    LayoutParams.WRAP_CONTENT, 0.8f));
			name.setText(a.getTitle());
			a.setID(i);
			name.setTextColor(Color.WHITE);
			name.setTextSize(Config.dpToPx(10));
			name.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			name.setPadding(Config.dpToPx(25), Config.dpToPx(10), Config.dpToPx(15), Config.dpToPx(10));

			tb.setPadding(Config.dpToPx(25), Config.dpToPx(10), Config.dpToPx(15), Config.dpToPx(10));
			tb.setBackgroundColor(Color.argb(175, 0, 0, 0));
			tb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = Config.context.getPackageManager().getLaunchIntentForPackage(a.getPackageName());

					if (intent != null) {
						startActivity(intent);
					}
					tb.setBackgroundColor(Color.TRANSPARENT);
				}

			});
			tb.addView(name, new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			gv.addView(tb, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			/*
			 * This is the gray line...
			 */
			TableRow tl = new TableRow(Config.context);
			tl.setBackgroundResource(R.layout.customborder);
			gv.addView(tl);
			i++;
		}

		return view;
	}

	public List<Apps> listAllw2() {
		List<Apps> apps = new ArrayList<Apps>();
		List<ApplicationInfo> appinfo = pm.getInstalledApplications(0);

		Apps app;
		int i = 0;
		List<String> appnames = appNames();
		for (int j = 0; j < appinfo.size(); j++) {
			if (Config.in_array(appnames, appinfo.get(j).packageName)) {
				app = new Apps();
				app.setPackageName(appinfo.get(j).packageName);
				app.setTitle(appinfo.get(j).loadLabel(pm).toString());
				apps.add(app);
			}
		}
		return apps;
	}

	public List appNames() {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> packs = pm.queryIntentActivities(mainIntent, 0);

		List<String> appNames = new ArrayList<String>(packs.size());
		for (ResolveInfo ai : packs) {
			appNames.add(ai.activityInfo.packageName.toString());
		}
		return appNames;
	}
}
