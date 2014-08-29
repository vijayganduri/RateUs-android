package com.vijayganduri.cybrilla.rateus.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.vijayganduri.cybrilla.rateus.AppConstants;
import com.vijayganduri.cybrilla.rateus.MainApplication;
import com.vijayganduri.cybrilla.rateus.R;
import com.vijayganduri.cybrilla.rateus.beans.Rating;
import com.vijayganduri.cybrilla.rateus.beans.Vote;
import com.vijayganduri.cybrilla.rateus.dao.VoteDao;
import com.vijayganduri.cybrilla.rateus.enums.ServiceEnum;

public class AllServicesActivity extends SherlockActivity implements OnItemClickListener{

	private long userid;
	private ListView listView;
	private ServicesArrayAdapter adapter;

	private MainApplication app;
	private VoteDao voteDao;

	List<Rating> ratings = new ArrayList<Rating>();
	private ProgressDialog mDialog;

	static final int REQUEST_RATING = 1;

	private static final String TAG = AllServicesActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_services);

		//return as there is no active session for recording the vote
		if(getIntent()==null || getIntent().getExtras()==null || 
				getIntent().getLongExtra(AppConstants.INTENT_USER_ID, -1)==-1){
			finish();
			return;
		}

		userid = getIntent().getLongExtra(AppConstants.INTENT_USER_ID, -1);
		listView = (ListView)findViewById(R.id.listview);

		adapter = new ServicesArrayAdapter(this);

		for(ServiceEnum service : ServiceEnum.values()){
			Rating rating = new Rating();
			rating.setServiceType(service.getLabel());
			rating.setRating(0);
			ratings.add(rating);
			adapter.add(rating);
		}

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		app = (MainApplication)getApplication();
		voteDao = app.getVoteDao();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, RateActivity.class);
		intent.putExtra(AppConstants.INTENT_RATING_SERVICE_INFO, ratings.get(position));
		startActivityForResult(intent, REQUEST_RATING);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_RATING){
			if(resultCode== RESULT_OK){
				if(data!=null){
					Rating rating = (Rating)data.getSerializableExtra(AppConstants.INTENT_RATING_SERVICE_INFO);
					if(rating!=null){
						updateItem(rating);
					}
				}
			}
		}
	}

	private void updateItem(Rating updateRating){
		for(Rating rating:ratings){
			if(rating.getServiceType().equalsIgnoreCase(updateRating.getServiceType())){
				rating.setAdditionalInfo(updateRating.getAdditionalInfo());
				rating.setRating(updateRating.getRating());
				break;
			}
		}
		//To update the specific single row
		int start = listView.getFirstVisiblePosition();
		for(int i=start, j=listView.getLastVisiblePosition();i<=j;i++){
			Rating rating = (Rating)listView.getItemAtPosition(i);
			if(updateRating.getServiceType().equals(rating.getServiceType())){
				View view = listView.getChildAt(i-start);
				listView.getAdapter().getView(i, view, listView);
				break;
			}
		}
	}

	/**
	 *  We don't store user session details until he votes. 
	 *  Just clear the session details on exiting the screen
	 */
	@Override
	public void onBackPressed() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setMessage(R.string.exit_vote_msg)
		.setTitle(R.string.exit_vote);
		alert.setPositiveButton(R.string.yes, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearSessionAndExit(false);
			}
		});
		alert.setNegativeButton(R.string.cancel, null);
		alert.show();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.services, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()== R.id.action_submit){
			submit();
		}
		return super.onOptionsItemSelected(item);
	}

	private void submit(){
		//Need to fill all before submitting
		if(!areAllFieldsFilled()){
			showToast(getText(R.string.warn_fill_all));
			return;
		}
		new SaveAsyncTask().execute();
	}

	private class SaveAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			showDialog(getText(R.string.info_submit));
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {		
			long updatedAt = System.currentTimeMillis();
			for(Rating rating : ratings){
				Vote vote = new Vote();
				vote.setUserid(userid);
				vote.setUpdatedAt(updatedAt);
				vote.setServiceType(rating.getServiceType());
				vote.setRating(rating.getRating());
				vote.setAdditionalInfo(rating.getAdditionalInfo());
				try {
					voteDao.create(vote);
				} catch (SQLException e) {
					Log.e(TAG, "Could not write to db "+e);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result){
			clearSessionAndExit(true);
			super.onPostExecute(result);
		}

	}

	private void clearSessionAndExit(boolean redirectResults){
		cancelDialog();
		finish();
		if(redirectResults){
			Intent intent = new Intent(this, DashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(AppConstants.INTENT_SHOW_RESULTS, true);
			startActivity(intent);
		}
	}

	private boolean areAllFieldsFilled(){
		for(Rating rating :ratings){
			if(rating.getRating()==0){
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(AppConstants.INTENT_USER_ID, userid);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		userid = savedInstanceState.getLong(AppConstants.INTENT_USER_ID, -1);
	}

	public class ServicesArrayAdapter extends ArrayAdapter<Rating> {

		private LayoutInflater inflater;

		public ServicesArrayAdapter(Context context) {
			super(context, android.R.layout.list_content);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Viewholder holder;

			if(convertView == null){
				holder = new Viewholder();
				convertView = inflater.inflate(R.layout.services_list_item, parent, false);
				holder.itemRating = (TextView) convertView.findViewById(R.id.item_rating);
				holder.itemTitle = (TextView) convertView.findViewById(R.id.item_title);

				convertView.setTag(holder);
			}else{
				holder = (Viewholder)convertView.getTag();
			}

			Rating item = getItem(position);

			holder.itemRating.setText(Integer.toString(item.getRating()));
			holder.itemTitle.setText(item.getServiceType());
			if(item.getRating()==0){
				holder.itemRating.setSelected(false);
			}else{
				holder.itemRating.setSelected(true);
			}

			return convertView;
		}

	}

	static class Viewholder{
		TextView itemRating;
		TextView itemTitle;
	}


	private void showToast(CharSequence msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void showDialog(CharSequence msg){
		cancelDialog();
		mDialog = new ProgressDialog(this);
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
