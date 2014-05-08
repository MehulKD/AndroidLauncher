package com.kregelbagel.android.core;

import java.util.Map;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

public class Apps {
	private String title;
	private String packageName;
	private String versionName;
	private int versionCode;
	private String description;
	private int id;
	// ordinary getters and setters

	public String getTitle() {
		return title;
	}

	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public class AppViewHolder {

		public TextView mTitle;
		public ImageView mIcon;

		/**
		 * Sets the text to be shown as the app's title
		 * 
		 * @param title
		 *          the text to be shown inside the list row
		 */
		public void setTitle(String title) {
			mTitle.setText(title);
		}

		/**
		 * Sets the icon to be shown next to the app's title
		 * 
		 * @param img
		 *          the icon drawable to be displayed
		 */
		public void setIcon(Drawable img) {
			if (img != null) {
				mIcon.setImageDrawable(img);
			}
		}
	}
  private static Map<String, Drawable> mIcons;

	public static void setIcons(Map<String, Drawable> icons) {
    mIcons = icons;
  }

}
