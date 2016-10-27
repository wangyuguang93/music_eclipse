package com.example.musicplay;

import com.example.musicplay.R.id;

import android.content.Context;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MusicAdpater extends BaseAdapter{
	private Context context;
	private String Data[];
	private ListView mListview;
	private int ysq;
	private View view;
	private TextView tv1;
	private ImageView img_ysq;
	//private Context context;

	public MusicAdpater(Context context,String data[],ListView listview,int musicIndex) {
		// TODO Auto-generated constructor stub
		super();
		this.context=context;
		this.Data=data;
		this.mListview=listview;
		this.ysq=musicIndex;
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
		view=View.inflate(context, R.layout.music_list_item, null);
		tv1=(TextView) view.findViewById(R.id.tv_music_item);
		TextView tv_music_id=(TextView) view.findViewById(R.id.tv_music_id);
		img_ysq=(ImageView) view.findViewById(R.id.img_bfz);
		//tv_music_id.setText(""+position);		
		tv_music_id.setText(""+(position+1));
		if (ysq==position) {
			img_ysq.setImageDrawable(view.getResources().getDrawable(R.drawable.ysq));
			//img_ysq.setimg
			tv1.setText(Data[ysq]);
		}
		else {
			tv1.setText(Data[position]);
		}
		
		return view;
	}
	public void update(int posi,int posi2) {
		for(int i=0;i<Data.length-1;i++)
		{
			if (i==posi) {
				img_ysq.setImageDrawable(null);
				//img_ysq.setimg
				tv1.setText(Data[ysq]);
			}
			if (i==posi2) {
				img_ysq.setImageDrawable(view.getResources().getDrawable(R.drawable.ysq));
				//img_ysq.setimg
				tv1.setText(Data[ysq]);
			}
		}
		
	}
	
}
