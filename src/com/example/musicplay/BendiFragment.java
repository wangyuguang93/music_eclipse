package com.example.musicplay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BendiFragment extends Fragment{
	private View view;
	
	//构造函数
	public BendiFragment() {
		// TODO Auto-generated constructor stub
	}
	public BendiFragment(Context context) {
		// TODO Auto-generated constructor stub
	}
	
	public static interface Funhui{
		void fanhuiView(View view);		
	}
	Funhui funhui;
	public void setFunhui(Funhui funhui){
		this.funhui=funhui;
	}
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.bendi, container,false);
		
		funhui.fanhuiView(view);
		Log.d("kkk", "fanhuiListview");
		return view;
				
				
	}
	@TargetApi(23)
	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);
		try{
			funhui=(Funhui) context;
		}catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(context.toString()+"must implement OnArticleSelectedListene");
		}
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			funhui=(Funhui) activity;
		}catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListene");
		}
	}
}
