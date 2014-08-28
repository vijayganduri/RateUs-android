package com.vijayganduri.cybrilla.rateus.fragment;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.vijayganduri.cybrilla.rateus.MainApplication;
import com.vijayganduri.cybrilla.rateus.R;
import com.vijayganduri.cybrilla.rateus.beans.Vote;
import com.vijayganduri.cybrilla.rateus.dao.VoteDao;

public class ResultsFragment extends SherlockFragment
{

	private MainApplication app;

	private VoteDao voteDao;

	private static final String TAG = ResultsFragment.class.getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_results, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		app = (MainApplication)getActivity().getApplication();
		voteDao = app.getVoteDao();

		List<Vote> votes = voteDao.getAllVotes();
		if(votes!=null && votes.size()>0){
			for(Vote vote:votes){
				Log.d(TAG, "vote "+vote);
			}
		}
	}


}
