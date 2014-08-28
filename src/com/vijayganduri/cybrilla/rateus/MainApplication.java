package com.vijayganduri.cybrilla.rateus;

import android.app.Application;

import com.bugsense.trace.BugSenseHandler;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.vijayganduri.cybrilla.rateus.dao.DBHelper;
import com.vijayganduri.cybrilla.rateus.dao.UserDao;
import com.vijayganduri.cybrilla.rateus.dao.VoteDao;

public class MainApplication extends Application{

    private DBHelper dbHelper;
	private UserDao userDao;
	private VoteDao voteDao;
	
	private static final String TAG = MainApplication.class.getName();
	
	@Override
	public void onCreate() {		
		super.onCreate();
		BugSenseHandler.initAndStartSession(this, AppConstants.BUGSENSE_APPLICATION_KEY);
    	setupDao();
	}
	
	private void setupDao(){
		setupDBHelper();
		userDao = dbHelper.getUserDao();
		voteDao = dbHelper.getVoteDao();
	}
	
	private void setupDBHelper(){
    	if( dbHelper == null )
    		dbHelper = OpenHelperManager.getHelper( this, DBHelper.class );    	
	}

	public UserDao getUserDao(){
		if(userDao==null)
			setupDao();
		return userDao;
	}
	
	public VoteDao getVoteDao(){
		if(voteDao==null)
			setupDao();
		return voteDao;
	}
	
	public DBHelper getDBHelper(){
		if(dbHelper==null)
			setupDBHelper();
		return dbHelper;
	}
}
