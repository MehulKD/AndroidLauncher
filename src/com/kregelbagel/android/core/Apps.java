package com.kregelbagel.android.core;

import java.util.ArrayList;

public class Apps {
		String appName;
		String appIcon;
		static ArrayList<Apps> array = new ArrayList<Apps>();
		
		public void addItems(String key, String s) {
			Apps a = new Apps();
			a.appIcon = key;
			a.appName = s;
			array.add(a);
		}
		public static ArrayList<Apps> getItems(){
			return array;
		}
		public static int getArraySize(){
			return array.size();
		}
		public String getItem(int i){
			return array.get(i).appName;
		}

}
