package com.example.musicplay;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
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
	private TextView loginView;
	private View view;
	private Boolean is_denglu=false;
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
		view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		todayView = view.findViewById(R.id.tvToday);
		shoucanView = view.findViewById(R.id.tv_shoucan);
		settingView = view.findViewById(R.id.setting);
		exittesView = view.findViewById(R.id.exit);
		loginView=(TextView) view.findViewById(R.id.login);
		
		todayView.setOnClickListener(this);
		shoucanView.setOnClickListener(this);
		settingView.setOnClickListener(this);
		exittesView.setOnClickListener(this);
		loginView.setOnClickListener(this);
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
		case R.id.exit:// 退出
			Log.d("din", "login");
			activity.finish();			
			break;
		case R.id.login:// 登录
			if (!is_denglu) {
				Intent intent=new Intent();
				intent.setClass(activity, Login.class);
				startActivityForResult(intent, 1);
				activity.toggle();
			}
			
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){  
                String xingming = data.getStringExtra("xingming");//接收返回数据  
                loginView.setText(xingming);
                is_denglu=true;
             
        }  
	}
}
