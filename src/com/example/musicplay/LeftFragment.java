package com.example.musicplay;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
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
	private String xuehao,xm,xb,bj;
	private Intent userinfo;
	private ImageView headImage;
	public LeftFragment(MainActivity context) {
		// TODO Auto-generated constructor stub
		activity=context;
	}
	public LeftFragment() {
		// TODO Auto-generated constructor stub
		
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
		readinfo();
		return view;
	}
	
	public void readinfo() {
		// TODO Auto-generated method stub
		SharedPreferences info=activity.getSharedPreferences("info", Context.MODE_PRIVATE);
		xm=info.getString("xingming", null);
		is_denglu=info.getBoolean("is_denglu", false);
		
		if (xm!=null&&is_denglu) {
			loginView.setText(xm);
			xuehao=info.getString("xuehao", null);			
			xb=info.getString("xb", null);
			bj=info.getString("bj", null);
			String localIconNormal = xuehao; 
			 try{
			FileInputStream localStream = activity.openFileInput(localIconNormal); 
			 
			Bitmap bitmap = BitmapFactory.decodeStream(localStream);
			if (bitmap!=null) {
				Log.d("set_head","ok");
				headImage.setImageBitmap(bitmap);
			}
			
			 }catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
		}else {
			loginView.setText("登录/注册");
			headImage.setImageDrawable(view.getResources().getDrawable(R.drawable.touxiang));
		}
		
	}
	public void findViews(View view) {
		todayView = view.findViewById(R.id.tvToday);
		shoucanView = view.findViewById(R.id.tv_shoucan);
		settingView = view.findViewById(R.id.setting);
		exittesView = view.findViewById(R.id.exit);
		loginView=(TextView) view.findViewById(R.id.login);
		headImage = (ImageView) view. findViewById(R.id.touxiang);
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
		FragmentManager fragmentManager=getFragmentManager();
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
			}else {
				if (userinfo==null) {
				userinfo=new Intent(activity, User_info.class);
				}
	
				startActivity(userinfo);
			
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
                xuehao=data.getStringExtra("userName");
                xm=xingming;
                xb=data.getStringExtra("xb");
                bj=data.getStringExtra("bj");
                loginView.setText(xingming);
                is_denglu=true;
                //保存用户信息
                if(is_denglu){
            		SharedPreferences info=activity.getSharedPreferences("info", Context.MODE_PRIVATE);
            		Editor editor=info.edit();
            		editor.putString("xuehao", xuehao);
            		editor.putString("xingming",xingming);
            		editor.putString("xb", xb);
            		editor.putString("bj", bj);
            		editor.putBoolean("is_denglu", is_denglu);
            		editor.commit();
            		}
            		
             
        }  
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		readinfo();
		super.onResume();
	}
}
