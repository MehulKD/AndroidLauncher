package com.kregelbagel.android.drawer;

import java.util.ArrayList;
import java.util.List;

import com.kregelbagel.android.core.Config;
import com.kregelbagel.android.core.Apps;

import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Frag4 extends Fragment {

	ImageView ivIcon;

	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";

	public Frag4() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.drawer2, container, false);
		GridLayout gv = (GridLayout) view.findViewById(R.id.gridView1);
		List<ApplicationInfo> appsList = new ArrayList<ApplicationInfo>();
		PackageManager pm = Config.context.getPackageManager();
		appsList = pm.getInstalledApplications(0);

		ArrayList<Apps> apps = new ArrayList<Apps>();
		Apps appsClass;
		for (ApplicationInfo appInfo : appsList) {
			String label = (String) appInfo.loadLabel(pm);
			// Drawable image = appInfo.loadIcon(pm);
			appsClass = new Apps();
			appsClass.addItems(label, label);
			apps.add(appsClass);
		}
		// ivIcon = (ImageView) view.findViewById(R.id.appicon);
		if (apps.size() < 3)
			for (int a = 0; a < 3; a++) {
				Apps object = new Apps();
				apps.add(object);
			}
		DisplayMetrics metrics = Config.context.getResources().getDisplayMetrics();
		float dp = 250f;
		float fpixels = metrics.density * dp;
		int pixels = (int) (fpixels + 0.5f);
		// TextView[] tvItemName = new TextView[apps.size()];
		for (int i = 0; i < apps.size(); i++) {
			TextView tvItemName = new TextView(Config.context);
			tvItemName.setId(i + 10);
			tvItemName.setText(apps.get(i).getItem(i));
			tvItemName.setBackgroundColor(Color.BLACK);
			tvItemName.setTextColor(Color.WHITE);
			tvItemName.setLayoutParams(new LayoutParams(pixels, LayoutParams.WRAP_CONTENT));
			// tvItemName.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
			// LayoutParams.WRAP_CONTENT));
			gv.addView(tvItemName);
			Config.makeHotToast(tvItemName.getText() + "");
		}

		// ivIcon.setImageDrawable(view.getResources().getDrawable(
		// getArguments().getInt(IMAGE_RESOURCE_ID)));
		return view;
	}
}
