package com.kregelbagel.android.core;

import android.content.Context;
import android.widget.Toast;

public class Config {
	public static Context context = null;

	public static void makeHotToast(String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}

	public void makeColdToast(String s) {
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}

	public void makeColdToast(int s) {
		Toast.makeText(context, " " + s + " ", Toast.LENGTH_LONG).show();
	}

}
