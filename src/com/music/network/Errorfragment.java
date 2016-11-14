package com.music.network;

import com.example.musicplay.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Errorfragment extends Fragment{
	private Context context;
	public Errorfragment() {
		// TODO Auto-generated constructor stub
	}
	public Errorfragment(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.net_error, container,false);
		
		return view;
	}
}
