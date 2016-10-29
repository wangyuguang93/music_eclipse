package com.example.musicplay;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View todayView;
	private View shoucanView;
	private View settingView;
	private View exittesView;
	private MainActivity activity;
	public LeftFragment(MainActivity context) {
		// TODO Auto-generated constructor stub
		activity=context;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		todayView = view.findViewById(R.id.tvToday);
		shoucanView = view.findViewById(R.id.tv_shoucan);
		settingView = view.findViewById(R.id.setting);
		exittesView = view.findViewById(R.id.exit);
		
		todayView.setOnClickListener(this);
		shoucanView.setOnClickListener(this);
		settingView.setOnClickListener(this);
		exittesView.setOnClickListener(this);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		
		switch (v.getId()) {
		case R.id.tvToday: // 今日天气
			break;
		case R.id.exit:// 收藏
			activity.finish();
			
			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent);
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment);
		}
	}
}
