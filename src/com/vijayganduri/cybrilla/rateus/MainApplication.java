package com.vijayganduri.cybrilla.rateus;

import android.app.Application;

import com.bugsense.trace.BugSenseHandler;

public class MainApplication extends Application{

	private static final String TAG = MainApplication.class.getName();
	
	@Override
	public void onCreate() {		
		super.onCreate();
		BugSenseHandler.initAndStartSession(this, AppConstants.BUGSENSE_APPLICATION_KEY);
	}
	

}
