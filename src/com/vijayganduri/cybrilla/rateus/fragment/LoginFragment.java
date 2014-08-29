package com.vijayganduri.cybrilla.rateus.fragment;

import java.math.BigInteger;
import java.sql.SQLException;

import org.bson.types.ObjectId;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.vijayganduri.cybrilla.rateus.AppConstants;
import com.vijayganduri.cybrilla.rateus.MainApplication;
import com.vijayganduri.cybrilla.rateus.R;
import com.vijayganduri.cybrilla.rateus.activity.AllServicesActivity;
import com.vijayganduri.cybrilla.rateus.beans.User;
import com.vijayganduri.cybrilla.rateus.dao.UserDao;

public class LoginFragment extends SherlockFragment
{
	
	private ImageButton signinBtn;
	private ProgressDialog mDialog;
	
	private MainApplication app;
	private UserDao userDao;

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
		
		signinBtn = (ImageButton) view.findViewById(R.id.signinBtn);
		signinBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startSignin();
			}
		});
	}
	
	private void startSignin(){
		//TODO setup google + auth		
		new RegisterUserAsyncTask().execute();
	}
	

	private class RegisterUserAsyncTask extends AsyncTask<Void, Void, User>{

		@Override
		protected void onPreExecute() {
			showDialog(getText(R.string.info_signing));
			super.onPreExecute();
		}

		@Override
		protected User doInBackground(Void... params) {
			//After successful Google + signin need to save it to db			
			return onLoginSuccess();
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
	private User onLoginSuccess(){
		
		User user = new User();
		user.setEmail("xyz");//TODO Fill with exact content
		user.setName("abc");
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
	
}
