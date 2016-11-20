package com.music.download;
import com.example.musicplay.MainActivity;
import com.example.musicplay.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DownloadFragment extends Fragment{
	private View view;
	private ListView downloadlistview;
	private MainActivity context;
	private DownloadAdpater downloadadpater;
	public DownloadFragment(MainActivity context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.download, null);
		findid(view);
		return view;
	}
	
	public void findid(View view) {
		downloadlistview=(ListView) view.findViewById(R.id.downloadlistView);
		 downloadadpater=new DownloadAdpater(context);
		 downloadlistview.setAdapter(downloadadpater);
	}
}
