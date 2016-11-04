package com.example.musicplay;

import java.util.List;

import com.example.musicplay.R.id;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MusicAdpater extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private String Data[];
	private int ysq;
	private Bitmap bitmap[];
	//private View view;
//	private TextView tv1;
//	private ImageView img_ysq,imaglab;
	private int musicid[];
	private	long almid[];
	//private Context context;

	public MusicAdpater(Context context,String data[],ListView listview,int musicIndex,int musicid[],long almid[]) {
		// TODO Auto-generated constructor stub
		super();
		 mInflater = LayoutInflater.from(context);
		this.context=context;
		this.Data=data;
		this.ysq=musicIndex;
		this.musicid=musicid;
		this.almid=almid;
		bitmap=new Bitmap[Data.length];
		for(int i=0;i<Data.length;i++){
		bitmap[i] = MediaUtil.getArtwork(context, musicid[i],almid[i], true, true);
		}
		
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
		ViewHolder holder;
		if (convertView == null) {
		
		//view=View.inflate(context, R.layout.music_list_item,null );
		convertView=mInflater.inflate(R.layout.music_list_item, null);
		holder = new ViewHolder();
		
		//view=view.inflate(context, resource, root)
		holder.tv1=(TextView)convertView.findViewById(R.id.tv_music_item);
		holder.tv_music_id=(TextView) convertView.findViewById(R.id.tv_music_id);
		holder.img_ysq=(ImageView) convertView.findViewById(R.id.img_bfz);
		holder.imaglab=(ImageView) convertView.findViewById(R.id.imaglab);
		
		
		convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		//tv_music_id.setText(""+position);		
		holder.tv_music_id.setText(""+(position+1)+".");
//		if (ysq==position) {
//			Bitmap bitmap = MediaUtil.getArtwork(context, musicid[ysq],almid[ysq], true, true);
//			holder.imaglab.setImageBitmap(bitmap);
//			holder.img_ysq.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ysq));
//			//img_ysq.setimg
//			holder.tv1.setText(Data[ysq]);
//		}
//		else {
			
		
		
			
			holder.tv1.setText(Data[position]);			
			holder.imaglab.setImageBitmap(bitmap[position]);	
//			System.out.println(almid[position]);
//		}
		
		 convertView.setTag(holder);
		
		return convertView;
	}
	public void update(int posi,int posi2) {
//		ViewHolder viewHolder1=new ViewHolder();
//		for(int i=0;i<Data.length-1;i++)
//		{
//			if (i==posi) {
//				viewHolder1.img_ysq.setImageDrawable(null);
//				//img_ysq.setimg
//				viewHolder1.tv1.setText(Data[ysq]);
//			}
//			if (i==posi2) {
//				viewHolder1.img_ysq.setImageDrawable(view.getResources().getDrawable(R.drawable.ysq));
//				//img_ysq.setimg
//				viewHolder1.tv1.setText(Data[ysq]);
//			}
//		}
		
	}
	
	 static class ViewHolder {
		 private TextView tv1;
		private ImageView img_ysq,imaglab;
		private TextView tv_music_id;
     }
	
	 
	 
}
