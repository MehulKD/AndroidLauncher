package com.kregelbagel.android.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.kregelbagel.android.core.Config;
import com.kregelbagel.android.core.Apps;
import com.kregelbagel.android.drawer.R;
import com.kregelbagel.android.drawer.R.id;
import com.kregelbagel.android.drawer.R.layout;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AppsDrawerFragment extends Fragment {
	ImageView imgview;
	PackageManager pm;

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
		List<Apps> loadedApps = loadInstalledApps(false);
		Collections.sort(loadedApps, new Comparator<Apps>() {

			@Override
			public int compare(Apps lhs, Apps rhs) {
				// TODO Auto-generated method stub
				return lhs.getTitle().compareTo(rhs.getTitle());
			}
		});
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
			tb.setBackgroundColor(Color.argb(175, 0, 0,0));
			tb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = Config.context.getPackageManager().getLaunchIntentForPackage(
					    a.getPackageName());

					if (intent != null) {
						startActivity(intent);
					}
					tb.setBackgroundColor(Color.TRANSPARENT);
				}

			});
			tb.addView(name, new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,
			    LayoutParams.WRAP_CONTENT));

			gv.addView(tb, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
			    LayoutParams.WRAP_CONTENT));
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

	private List<Apps> loadInstalledApps(boolean includeSysApps) {
		List<Apps> apps = new ArrayList<Apps>();

		// the package manager contains the information about all installed apps
		PackageManager packageManager = Config.context.getPackageManager();

		List<PackageInfo> packs = packageManager.getInstalledPackages(0); // PackageManager.GET_META_DATA

		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			ApplicationInfo a = p.applicationInfo;
			// skip system apps if they shall not be included
			if ((!includeSysApps) && ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
				continue;
			}
			Apps app = new Apps();
			app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
			app.setPackageName(p.packageName);
			app.setVersionName(p.versionName);
			app.setVersionCode(p.versionCode);
			CharSequence description = p.applicationInfo.loadDescription(packageManager);
			app.setDescription(description != null ? description.toString() : "");
			apps.add(app);
		}
		return apps;
	}

	
}
