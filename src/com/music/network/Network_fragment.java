package com.music.network;

import com.example.musicplay.R;
import com.example.musicplay.BendiFragment.Funhui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Network_fragment extends Fragment{
	
private View view;
private Context context;
	
	//构造函数
	public Network_fragment() {
		// TODO Auto-generated constructor stub
	}
	public Network_fragment(Context context) {
		// TODO Auto-generated constructor stub
	}
	
	public static interface Funhui_net{
		void fanhuiView_net(View view);		
	}
	Funhui_net funhuinet;
	public void setFunhui(Funhui_net funhuinet){
		this.funhuinet=funhuinet;
	}
	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	View view=inflater.inflate(R.layout.network, container,false);
	funhuinet.fanhuiView_net(view);
	return view;
}
@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			funhuinet=(Funhui_net) context;
		}catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(context.toString()+"must implement OnArticleSelectedListene");
		}

	}
@TargetApi(23)
@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);
		try{
			funhuinet=(Funhui_net) context;
		}catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(context.toString()+"must implement OnArticleSelectedListene");
		}
	}
}
