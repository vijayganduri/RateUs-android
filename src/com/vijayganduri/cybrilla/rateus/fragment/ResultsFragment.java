package com.vijayganduri.cybrilla.rateus.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.vijayganduri.cybrilla.rateus.MainApplication;
import com.vijayganduri.cybrilla.rateus.R;
import com.vijayganduri.cybrilla.rateus.beans.Vote;
import com.vijayganduri.cybrilla.rateus.dao.VoteDao;
import com.vijayganduri.cybrilla.rateus.enums.ServiceEnum;

public class ResultsFragment extends SherlockFragment
{

	private TableLayout tl; 

	private MainApplication app;
	private VoteDao voteDao;
	private List<ServiceRow> rows = new ArrayList<ServiceRow>();
	
    static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");

	private static long pastMonthDate = Long.valueOf(dayFormat.format(new Date().getTime()-(30*24*60*60*1000)));
	private static long pastYearDate = Long.valueOf(dayFormat.format(new Date().getTime()-(365*24*60*60*1000)));
	
	private static final String TAG = ResultsFragment.class.getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_results, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		tl = (TableLayout) view.findViewById(R.id.tableLayout);

		app = (MainApplication)getActivity().getApplication();
		voteDao = app.getVoteDao();

		fetchVotes();		
	}

	private void fetchVotes(){
		List<Vote> votes = voteDao.getAllVotes();
		
		for(ServiceEnum service :ServiceEnum.values()){
			List<Integer> yearly = new ArrayList<>();
			List<Integer> monthly = new ArrayList<>();
			
			for(Vote vote:votes){
				if(service.getLabel().equals(vote.getServiceType())){
					if(isCreatedLastMonth(vote)){
						monthly.add(vote.getRating());
					}else if(!isCreatedLastYear(vote)){
						continue;
					}
					yearly.add(vote.getRating());
				}
			}
			int monthlySum = 0;
			for(int val : monthly){
				monthlySum+=val;
			}
			int yearlySum = 0;
			for(int val : yearly){
				yearlySum+=val;
			}
			int mtd = yearly.size()>0?monthlySum/monthly.size():0;
			int ytd = yearly.size()>0?yearlySum/yearly.size():0;
			ServiceRow value = new ServiceRow(service.getLabel(), mtd, ytd);
			rows.add(value);
		}

		constructRows();
	}
	
	private boolean isCreatedLastMonth(Vote vote){
		return vote.getUpdatedAt()-pastMonthDate>0;
	}
	
	private boolean isCreatedLastYear(Vote vote){
		return vote.getUpdatedAt()-pastYearDate>0;
	}
	
	private void constructRows(){

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		for(int i=0;i<rows.size();i++){
			ServiceRow value = rows.get(i);
			TableRow row = (TableRow)inflater.inflate(R.layout.results_row_item, null);
			TextView service = (TextView)row.findViewById(R.id.title);
			TextView mtd = (TextView)row.findViewById(R.id.mtd);
			TextView ytd = (TextView)row.findViewById(R.id.ytd);
			
			if(i%2==0){
				service.setBackgroundResource(R.color.results_service2);
				mtd.setBackgroundResource(R.color.results_mtd2);
				ytd.setBackgroundResource(R.color.results_ytd2);
			}else{
				service.setBackgroundResource(R.color.results_service1);
				mtd.setBackgroundResource(R.color.results_mtd1);
				ytd.setBackgroundResource(R.color.results_ytd1);				
			}
			service.setText(value.servicename);
			mtd.setText(String.valueOf(value.MTD));
			ytd.setText(String.valueOf(value.YTD));
			
			tl.addView(row);
		}
	}

	public class ServiceRow{
		String servicename;
		int MTD;
		int YTD;

		public ServiceRow(String serviceName, int MTD, int YTD){
			this.servicename = serviceName;
			this.MTD = MTD;
			this.YTD = YTD;
		}

	}

}
