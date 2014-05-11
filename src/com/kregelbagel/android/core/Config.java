package com.kregelbagel.android.core;

import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Config {
	public static Context context = null;
	public static FragmentManager fragmentManager = null;

	public static void makeHotToast(String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}

	public void makeColdToast(String s) {
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}

	public void makeColdToast(int s) {
		Toast.makeText(context, " " + s + " ", Toast.LENGTH_LONG).show();
	}

	public static void setBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter.isEnabled()) {
			makeHotToast("Bluetooth Disabled!");
			mBluetoothAdapter.disable();
		} else {
			makeHotToast("Bluetooth Enabled!");
			mBluetoothAdapter.enable();
		}
	}
	public static boolean bluetoothStatus(){
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter.isEnabled())
			return true;
		else 
			return false;
	}
	public static int dpToPx(int dp) {
		DisplayMetrics displayMetrics = Config.context.getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}
}
