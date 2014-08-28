package com.vijayganduri.cybrilla.rateus.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.vijayganduri.cybrilla.rateus.AppConstants;
import com.vijayganduri.cybrilla.rateus.R;

public class RateActivity extends SherlockActivity {
	
	private int serviceId;
	private String serviceType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate);
		
		//return as there is no active session for recording the vote
		if(getIntent()==null ||  getIntent().getExtras()==null || 
				getIntent().getIntExtra(AppConstants.INTENT_RATING_SERVICE_ID, -1)==-1 ||
				getIntent().getStringExtra(AppConstants.INTENT_RATING_SERVICE_TYPE)==null ){
			finish();
			return;
		}

		serviceId = getIntent().getIntExtra(AppConstants.INTENT_RATING_SERVICE_ID, -1);
		serviceType = getIntent().getStringExtra(AppConstants.INTENT_RATING_SERVICE_TYPE);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.rate, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(AppConstants.INTENT_RATING_SERVICE_ID, serviceId);
		outState.putString(AppConstants.INTENT_RATING_SERVICE_TYPE, serviceType);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		serviceId = savedInstanceState.getInt(AppConstants.INTENT_RATING_SERVICE_ID, -1);
		serviceType = savedInstanceState.getString(AppConstants.INTENT_RATING_SERVICE_TYPE);
	}
	
}
