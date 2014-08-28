package com.vijayganduri.cybrilla.rateus.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.CirclePageIndicator;
import com.vijayganduri.cybrilla.rateus.R;
import com.vijayganduri.cybrilla.rateus.fragment.LoginFragment;
import com.vijayganduri.cybrilla.rateus.fragment.ResultsFragment;

public class DashboardActivity extends SherlockFragmentActivity {

	private FragmentAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator) findViewById(R.id.circle_indicator);
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(Fragment.instantiate(this, LoginFragment.class.getName()));
		fragments.add(Fragment.instantiate(this, ResultsFragment.class.getName()));

		mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);

		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);

		final float density = getResources().getDisplayMetrics().density;
		mIndicator.setRadius(5 * density);

	}

	public class FragmentAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragmentInfos = null;

		public FragmentAdapter( FragmentManager fragmentManager, List<Fragment> fragments) {
			super( fragmentManager );
			this.fragmentInfos = fragments;
		}

		@Override
		public int getCount() {
			if( fragmentInfos != null ){
				return fragmentInfos.size();
			}else{
				return 1;
			}
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentInfos.get( position );
		}

	}

}
