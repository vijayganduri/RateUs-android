package com.vijayganduri.cybrilla.rateus.fragment;

import java.math.BigInteger;
import java.sql.SQLException;

import org.bson.types.ObjectId;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.vijayganduri.cybrilla.rateus.AppConstants;
import com.vijayganduri.cybrilla.rateus.MainApplication;
import com.vijayganduri.cybrilla.rateus.R;
import com.vijayganduri.cybrilla.rateus.activity.AllServicesActivity;
import com.vijayganduri.cybrilla.rateus.beans.User;
import com.vijayganduri.cybrilla.rateus.dao.UserDao;

/**
 * Google plus signin based on http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1/
 * @author Vijay
 *
 */
public class LoginFragment extends SherlockFragment implements ConnectionCallbacks, OnConnectionFailedListener
{

	private SignInButton signinBtn;
	private ProgressDialog mDialog;

	private MainApplication app;
	private UserDao userDao;

	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;

	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;

	private static final String TAG = LoginFragment.class.getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		app = (MainApplication)getActivity().getApplication();
		userDao = app.getUserDao();

		signinBtn = (SignInButton) view.findViewById(R.id.signinBtn);
		signinBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startSignin();
			}
		});		

		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API, null)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}	

	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private void startSignin(){
		showDialog(getString(R.string.info_signing));
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	private class RegisterUserAsyncTask extends AsyncTask<String, Void, User>{

		@Override
		protected void onPreExecute() {
			showDialog(getText(R.string.info_signing));
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(String... params) {
			
			//After successful Google + signin need to save it to db			
			return onLoginSuccess(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(User user){			
			invokeForVoting(user);
			super.onPostExecute(user);
		}

	}

	/*
	 * Will be invoked after Google plus signin is successful
	 * Store the user to internal db
	 */
	private User onLoginSuccess(String name, String email){

		User user = new User();
		user.setEmail(email);
		user.setName(name);
		String id = new ObjectId().toStringMongod();
		BigInteger idValue = new BigInteger(id, 16);
		user.setId(idValue.longValue());

		try {
			user = userDao.createIfNotExists(user);
		} catch (SQLException e) {
			e.printStackTrace();
			showToast(getString(R.string.warn_login_fail));
			return null;
		}
		return user;
	}

	private void onLoginFailed(){
		showToast(getString(R.string.warn_login_fail));
	}	

	/*
	 * Start the AllServices screen for user to vote.
	 * User should rate immediately. On pressing back user session gets terminated and needs to relogin
	 */
	private void invokeForVoting(User user){
		cancelDialog();
		Intent intent = new Intent(getActivity(), AllServicesActivity.class);
		intent.putExtra(AppConstants.INTENT_USER_ID, user.getId());
		startActivity(intent);
	}

	private void showToast(CharSequence msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	private void showDialog(CharSequence msg){
		cancelDialog();
		mDialog = new ProgressDialog(getActivity());
		mDialog.setMessage(msg);
		mDialog.setCancelable(true);
		mDialog.show();
	}

	private void cancelDialog(){
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.cancel();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(),
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		getProfileInformation();
	}
	
	/**
	 * Fetching user's information name, email
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				new RegisterUserAsyncTask().execute(personName, email);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// proceed for saving in internal db
		new RegisterUserAsyncTask().execute();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		Toast.makeText(getActivity(), "User login failed", Toast.LENGTH_LONG).show();
		cancelDialog();
	}
}
