package com.vijayganduri.cybrilla.rateus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.vijayganduri.cybrilla.rateus.AppConstants;
import com.vijayganduri.cybrilla.rateus.R;
import com.vijayganduri.cybrilla.rateus.beans.Rating;

public class RateActivity extends SherlockActivity implements OnSeekBarChangeListener{
	
	private Rating rating;
	
	private TextView serviceTitle;
	private TextView ratingValue;
	private EditText msgBox;
	private SeekBar ratingBar;
	
	private static final int RATE_MIN = 1;
	private static final int RATE_MAX = 10;
	private static final int RATE_DEFAULT = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//return as there is no active session for recording the vote
		if(getIntent()==null ||  getIntent().getExtras()==null || 
				getIntent().getSerializableExtra(AppConstants.INTENT_RATING_SERVICE_INFO)==null ){
			finish();
			return;
		}

		inflateCustomActionBar();
		setContentView(R.layout.activity_rate);
		
		rating = (Rating) getIntent().getSerializableExtra(AppConstants.INTENT_RATING_SERVICE_INFO);
		
		serviceTitle = (TextView)findViewById(R.id.textView1);
		ratingValue = (TextView)findViewById(R.id.textView2);
		msgBox = (EditText)findViewById(R.id.editText1);
		ratingBar = (SeekBar)findViewById(R.id.seekBar1);

		serviceTitle.setText(rating.getServiceType());
		if(rating.getAdditionalInfo()!=null){
			msgBox.setText(rating.getAdditionalInfo());
		}
		
		ratingBar.setMax(RATE_MAX);
		ratingBar.setOnSeekBarChangeListener(this);
		if(rating.getRating()==0){
			rating.setRating(RATE_DEFAULT);//setting default value of 50
		}
		ratingBar.setProgress(rating.getRating());
	}
	
	private void inflateCustomActionBar(){

        // BEGIN_INCLUDE (inflate_set_custom_view)
        // Inflate a "Done/Cancel" custom action bar view.
        final LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_cancel, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	done();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        // END_INCLUDE (inflate_set_custom_view)
	}
	
	private void cancel(){
		setResult(RESULT_CANCELED, null);
		finish();		
	}

	private void done(){
		
		rating.setAdditionalInfo(msgBox.getText().toString());
		
		Intent data = new Intent();
		data.putExtra(AppConstants.INTENT_RATING_SERVICE_INFO, rating);
		setResult(RESULT_OK, data);
		finish();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(AppConstants.INTENT_RATING_SERVICE_INFO, rating);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		rating = (Rating)savedInstanceState.getSerializable(AppConstants.INTENT_RATING_SERVICE_INFO);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(progress==0){
			progress = RATE_MIN;//limiting to min value
		}
		rating.setRating(progress);
		ratingValue.setText(String.valueOf(progress));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
}
