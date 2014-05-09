package com.kregelbagel.android.fragments;

import com.kregelbagel.android.core.Config;
import com.kregelbagel.android.drawer.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class SettingsFragment extends Fragment {
	Switch bluetooth_toggle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settingslayout, container, false);
		bluetooth_toggle = (Switch) view.findViewById(R.id.bluetooth_toggle);
		bluetooth_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (Config.bluetoothStatus()) {
					isChecked = true;
					buttonView.setChecked(isChecked);
				} else {
					isChecked = false;
					buttonView.setChecked(isChecked);
				}
				if (isChecked)
					Config.setBluetooth();

			}
		});
		return view;
	}
}
