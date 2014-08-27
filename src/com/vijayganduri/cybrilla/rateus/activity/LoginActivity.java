package com.vijayganduri.cybrilla.rateus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.vijayganduri.cybrilla.rateus.R;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
