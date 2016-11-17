package com.example.musicplay;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.musicplay.R.id;
import com.music.bendi.Bendi_music;
import com.music.download.Net_music_download;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MusicAdpater extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private String Data[];
	private int position;
	private Bitmap bitmap[];
	private List<Bendi_music> bendi_musics;
	//private View view;
//	private TextView tv1;
//	private ImageView img_ysq,imaglab;
	private int musicid[];
	private	long almid[];
	private Bendi_music[] bm;
	//private Context context;

	public MusicAdpater(Context context,String data[],ListView listview,int musicIndex,int musicid[],long almid[]) {
		// TODO Auto-generated constructor stub
		super();
		 mInflater = LayoutInflater.from(context);
		this.context=context;
		this.Data=data;
		this.musicid=musicid;
		this.almid=almid;
		bitmap=new Bitmap[Data.length];
		for(int i=0;i<Data.length;i++){
		bitmap[i] = MediaUtil.getArtwork(context, musicid[i],almid[i], true, true);
		}
		
	}
	public MusicAdpater(Context context,List<Bendi_music> bendi_musics) {
		// TODO Auto-generated constructor stub
		this.bendi_musics=bendi_musics;
		this.context=context;
		 mInflater = LayoutInflater.from(context);
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return Data.length;
		return bendi_musics.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		bm=bendi_musics.toArray(new Bendi_music[bendi_musics.size()]);
		this.position=position;
		ViewHolder holder;
		if (convertView == null) {
		
		//view=View.inflate(context, R.layout.music_list_item,null );
		convertView=mInflater.inflate(R.layout.music_list_item, null);
		holder = new ViewHolder();
		
		//view=view.inflate(context, resource, root)
		holder.tv1=(TextView)convertView.findViewById(R.id.tv_music_item);
		holder.tv_music_id=(TextView) convertView.findViewById(R.id.tv_music_id);
		
		holder.imaglab=(ImageView) convertView.findViewById(R.id.imaglab);
		holder.imgmore=(ImageView) convertView.findViewById(R.id.img_bfz);
		convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgmore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 showPopupMenu(v,position);
			}
		});
		
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
			
		
		
			
			holder.tv1.setText(bm[position].getDisName());			
			
		    Bitmap pic=bm[position].getBnendi_pic();
	        if (pic!=null) {
	        	holder.imaglab.setImageBitmap(bm[position].getBnendi_pic());
			}
	        else {
	        	holder.imaglab.setImageDrawable(context.getResources().getDrawable(R.drawable.deault_zhuanji));
			}
//			System.out.println(almid[position]);
//		}
		
		 convertView.setTag(holder);
		
		return convertView;
	}
	
	 static class ViewHolder {
		 private TextView tv1;
		private ImageView imaglab;
		private TextView tv_music_id;
		private ImageView imgmore;
     }
	
	 private void showPopupMenu(View view,final int index) {
		 // View当前PopupMenu显示的相对View的位置
		 PopupMenu popupMenu = new PopupMenu(context, view);
		 // menu布局
		 view.getId();
		
		 popupMenu.getMenuInflater().inflate(R.menu.bendi_option, popupMenu.getMenu());
		 // menu的item点击事件
		 popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
		  @Override
		  public boolean onMenuItemClick(MenuItem item) {
		 // Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_SHORT).show();
			  switch (item.getItemId()) {
			case R.id.bendi_option_delete:
				String deleteurl=bm[index].getUrl();
				String song=bm[index].getDisName();
				//Log.d("删除", ""+index);
				Log.d("删除", ""+deleteurl);
			//	Toast.makeText(context, song, Toast.LENGTH_SHORT).show();
				File deletefile=new File(deleteurl);
				bendi_musics.remove(index);
				MainActivity.deleteupdate();
				MainActivity.delete(deletefile);
				MediaScannerConnection.scanFile(context,
						new String[] { deleteurl },
						null, new MediaScannerConnection.OnScanCompletedListener() {
							public void onScanCompleted(String path, Uri uri) {
								Log.i("ExternalStorage", "Scanned " + path + ":");
								Log.i("ExternalStorage", "-> uri=" + uri);
//								Intent intent = new Intent();
//								intent.putExtra("msg", "updatemusicdate");
//								intent.setAction("main");
//								context.sendBroadcast(intent);
							}
						});
				break;
			case R.id.bendi_option_play:
				Intent intent = new Intent();
				intent.putExtra("msg", "bendiplay");
				intent.putExtra("bendiindex",index);
				intent.setAction("main");
				context.sendBroadcast(intent);
				break;
			case R.id.bendi_option_xiangqing:
				AlertDialog.Builder xiangqing=new AlertDialog.Builder(context);
				xiangqing.setTitle("详情");
				xiangqing.setMessage("文件名："+bm[index].getDisName()+"\n"+"专辑:"+bm[index].getAlbum()+"\n"+"大小:"+bm[index].getSize()/1024/1024+"M"+"\n"+"位置："+bm[index].getUrl());
				xiangqing.create().show();
				break;
			default:
				break;
			}
		  return false;
		  }
		 });
		 // PopupMenu关闭事件
		 popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
		  @Override
		  public void onDismiss(PopupMenu menu) {
		 // Toast.makeText(mContext, "关闭PopupMenu", Toast.LENGTH_SHORT).show();
		  }
		 });
		 popupMenu.show();
		 }
	 
	 
}
