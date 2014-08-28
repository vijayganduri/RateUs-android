package com.vijayganduri.cybrilla.rateus.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
		intent.putExtra(AppConstants.INTENT_RATING_SERVICE_ID, ratings.get(position).getRating());
		intent.putExtra(AppConstants.INTENT_RATING_SERVICE_TYPE, ratings.get(position).getServiceType());
		startActivityForResult(intent, REQUEST_RATING);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_RATING){
			if(resultCode== RESULT_OK){

			}
		}
	}
	
	@Override
	public void onBackPressed() {
		
		//TODO clear session and exit
		super.onBackPressed();
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
		}
		
		String updatedAt = String.valueOf(System.currentTimeMillis());
		for(Rating rating : ratings){
			Vote vote = new Vote();
			vote.setUserid(userid);
			vote.setUpdatedAt(updatedAt);
			vote.setServiceType(rating.getServiceType());
			vote.setRating(rating.getRating());
			voteDao.createOrUpdate(vote);
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
	
	private void showDialog(String msg){
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
