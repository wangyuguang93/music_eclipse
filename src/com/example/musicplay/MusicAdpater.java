package com.example.musicplay;

import android.content.Context;
import android.provider.ContactsContract.Contacts.Data;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MusicAdpater extends BaseAdapter{
	private Context context;
	private String Data[];
	private ListView mListview;
	//private Context context;

	public MusicAdpater(Context context,String data[],ListView listview) {
		// TODO Auto-generated constructor stub
		super();
		this.context=context;
		this.Data=data;
		this.mListview=listview;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=View.inflate(context, R.layout.music_list_item, null);
		TextView tv1=(TextView) view.findViewById(R.id.tv_music_item);
		tv1.setText(Data[position]);
		return view;
	}

}
