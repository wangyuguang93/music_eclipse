package com.music.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.base.Util.Base64Util;
import com.base.Util.MD5Utils;
import com.base.Util.music_API;
import com.example.musicplay.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class Getmusic implements music_API{
	//private NetKGmusicInfo info;
	private Boolean updateview=false;
	private Context context;
	// 回调info接口
	GetInfo getInfo = null;

		public interface GetInfo {
			public void getinfo(NetKGmusicInfo resualt);
		}

		public void setGetInfo(GetInfo getInfo) {

			this.getInfo = getInfo;
		}
	
	
	
	
	
	
	
	
	
	public Getmusic() {
		// TODO Auto-generated constructor stub
		//this.context=context;
//		
//		getmusicinfo(info);
//		getlrc(info);
//		getlrcdownload(info);
	}

	/**
	 * 获取歌曲信息	
	 * @author guang
	 *
	 */
	private class Getmusicinfo extends AsyncTask<Void, Void, NetKGmusicInfo> {
	private NetKGmusicInfo info;
	public Getmusicinfo(NetKGmusicInfo info) {
		// TODO Auto-generated constructor stub
		this.info=info;
	}
	@Override
	protected NetKGmusicInfo doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try{
		String addr =KgmusicInfo+info.getHash();				
		// Log.d("addr", addr);
		 URL url = new URL(addr);
		 InputStream kgmusicinfo = url.openConnection().getInputStream();
		
		 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		 byte[] buffer = new byte[1024];
		 int len = 0;
		 while( (len=kgmusicinfo.read(buffer)) != -1 ){
		 outStream.write(buffer, 0, len);
		 }
		 kgmusicinfo.close();
		 
		 String jason = new String(outStream.toByteArray());
		 JSONObject rootJson = new JSONObject(jason);
		 String status = rootJson.getString("status");
		 if(status.equals("1")){				
			 	info.setImgUrl(rootJson.getString("imgUrl"));
			 	info.setUrl128(rootJson.getString("url"));
				info.setReq_hash(rootJson.getString("req_hash"));
				
		
		 }else{
		 //Logger.e(TAG, "getLinks error errorCode=" + errorCode);
		 }
		 return info;
		 
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return info;
	}
	@Override
	protected void onPostExecute(NetKGmusicInfo result) {
		// TODO Auto-generated method stub
		getlink(result);
		super.onPostExecute(result);
	}
		
	}
	/**
	 * 获取歌词
	 */
	private class Getlrc extends AsyncTask<Void, Void, NetKGmusicInfo>{
		private NetKGmusicInfo info;
		public Getlrc(NetKGmusicInfo info) {
			// TODO Auto-generated constructor stub
			this.info=info;
		}
		@Override
		protected NetKGmusicInfo doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
			String slcyurl=Kglyc;
			slcyurl+= URLEncoder.encode(info.getSongname(), "UTF-8");
			
			 slcyurl +="&duration="+info.getDuration()+"000";			
					
					 URL lcyurl = new URL(slcyurl);
					 InputStream kglcy = lcyurl.openConnection().getInputStream();
					
					 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					 byte[] bufferlcy = new byte[1024];
					 int lenlcy = 0;
					 while( (lenlcy=kglcy.read(bufferlcy)) != -1 ){
					 outStream.write(bufferlcy, 0, lenlcy);
					 }
					 kglcy.close();
					 
					 String lcyjason = new String(outStream.toByteArray());
					 JSONObject lcyrootJson = new JSONObject(lcyjason);
					 String lcyinfo = lcyrootJson.getString("info");
					 if(lcyinfo.equals("OK")){	
						 	//JSONObject data = lcyrootJson.getJSONObject("data");
							JSONArray geciinfo = lcyrootJson.getJSONArray("candidates");
							JSONObject lcyitem = geciinfo.getJSONObject(0);
						 
						 	info.setAccesskey(lcyitem.getString("accesskey"));
						 	info.setLycid(lcyitem.getString("id"));

//							if (isgeci.equals(lcyitem.getString("song"))) {
//							//	Log.d("歌名相同", "歌名相同");
//								
//							}else {
//							//	Log.d("歌名不相同", "歌名不相同");
//								lycDownload(info);
//								isgeci=lcyitem.getString("song");
//							}
//					
					 }else{
					 //Logger.e(TAG, "getLinks error errorCode=" + errorCode);
					 }
					 return info;
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} 
			
			
			return info;
		}
		@Override
		protected void onPostExecute(NetKGmusicInfo result) {
			// TODO Auto-generated method stub
			getlrcdownload(result);
			super.onPostExecute(result);
		}
		
	}
	/**
	 * 下载歌词
	 */
	private class Getlrcdownload extends AsyncTask<Void, Void, NetKGmusicInfo>{
		private NetKGmusicInfo info;	
		public Getlrcdownload(NetKGmusicInfo info) {
			// TODO Auto-generated constructor stub
			this.info=info;
		}
		@Override
		protected NetKGmusicInfo doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String lcydownload =Kglycdownload+"&id="+info.getLycid()+"&&accesskey="+info.getAccesskey();			
				 Log.d("下载歌词文件", lcydownload);
					 URL downloadlcy = new URL(lcydownload);
					 InputStream lcyinputStream = downloadlcy.openConnection().getInputStream();
					
					 ByteArrayOutputStream lcyout = new ByteArrayOutputStream();
					 byte[] bufferlcydownload = new byte[1024];
					 int lenlcydownload = 0;
					 while( (lenlcydownload=lcyinputStream.read(bufferlcydownload)) != -1 ){
						 lcyout.write(bufferlcydownload, 0, lenlcydownload);
					 }
					 lcyinputStream.close();
					 
					 String lcyjasondoload = new String(lcyout.toByteArray());
					 JSONObject lcyrootJsondownload = new JSONObject(lcyjasondoload);
					 String lcyinfodownload = lcyrootJsondownload.getString("info");
					 if(lcyinfodownload.equals("OK")){				
						 	String geci=(lcyrootJsondownload.getString("content"));
						 	
						 	info.setFmt(lcyrootJsondownload.getString("fmt"));
							byte[] lcy=Base64Util.decode(geci);
							String geciname=info.getSongname()+"."+info.getFmt();
							//歌词缓存目录
							File lcylujiu=Environment.getExternalStorageDirectory();
							File lcydir=new File(lcylujiu+"/kgmusic/"+"/lrc/"+"/Cache/");
							if (!lcydir.exists()) {
								lcydir.mkdirs();
								
							}
							
							File lcygeci=new File(lcydir, geciname);
							FileOutputStream geciout=new FileOutputStream(lcygeci);
							geciout.write(lcy);
							geciout.close();
							geci=null;
							lcylujiu=null;
							lcydir=null;
							geciout=null;
							lcygeci=null;
							downloadlcy=null;
							bufferlcydownload=null;
							lcyrootJsondownload=null;
							lcyinputStream=null;
					
					 }else{
					 //Logger.e(TAG, "getLinks error errorCode=" + errorCode);
					 }	
				 return info;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			return info;
		}
		@Override
		protected void onPostExecute(NetKGmusicInfo result) {
			
			
			super.onPostExecute(result);
		}
		
	}
	private class GetLinks extends AsyncTask<Void, Void,NetKGmusicInfo >{
		private NetKGmusicInfo info;
		public GetLinks(NetKGmusicInfo info) {
			// TODO Auto-generated constructor stub
			this.info=info;
		}
		@Override
		protected NetKGmusicInfo doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				
				String hash320=info.getHash320();
				String hashsq=info.getSqhash();
				String key320=MD5Utils.getMD5(hash320+"kgcloud");
				String keysq=MD5Utils.getMD5(hashsq+"kgcloud");
				String addr320 = KgmusicDownload+"&hash="+hash320+"&key="+key320;
				String addrsp = KgmusicDownload+"&hash="+hashsq+"&key="+keysq;
				 Log.d("addr", addr320);
				 URL url320 = new URL(addr320);
				 URL urlsq = new URL(addrsp);
				 //获取320链接
				 InputStream ips320 = url320.openConnection().getInputStream();
				
				 ByteArrayOutputStream outStream320 = new ByteArrayOutputStream();
				 byte[] buffer320 = new byte[1024];
				 int len320 = 0;
				 while( (len320=ips320.read(buffer320)) != -1 ){
				 outStream320.write(buffer320, 0, len320);
				 }
				 ips320.close();
				 String jason320 = new String(outStream320.toByteArray());
				 JSONObject rootJson320 = new JSONObject(jason320);
				 String status320 = rootJson320.getString("status");
				 
				 if(status320.equals("1")){
					info.setUrl320(rootJson320.getString("url"));
					info.setExtName(rootJson320.getString("extName"));
				 }else{
				 //Logger.e(TAG, "getLinks error errorCode=" + errorCode);
				 }
				 //获取无损链接
				 InputStream ipssq = urlsq.openConnection().getInputStream();
					
				 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				 byte[] buffersq = new byte[1024];
				 int lensq = 0;
				 while( (lensq=ipssq.read(buffersq)) != -1 ){
				 outStream.write(buffersq, 0, lensq);
				 }
				 ipssq.close();
				 String jasonsq = new String(outStream.toByteArray());
				 JSONObject rootJsonsq = new JSONObject(jasonsq);
				 String statussq= rootJsonsq.getString("status");
				 
				 if(statussq.equals("1")){
					info.setUrlsq(rootJsonsq.getString("url"));
					info.setExtNamesq(rootJsonsq.getString("extName"));
				 }else{
				 //Logger.e(TAG, "getLinks error errorCode=" + errorCode);
				 }
				 
				 hash320=null;
				 hashsq=null;
				 key320=null;
				 keysq=null;
				addr320 = null;
				addrsp = null;
				url320=null;
				urlsq=null;
				ips320=null;
				ipssq=null;
				jasonsq=null;
				jason320=null;
				 rootJson320=null;
				 rootJsonsq=null;
				 status320=null;
				 statussq=null;
				 buffer320=null;
				 buffersq=null;
				 return info;
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			
			return info;
		}

		@Override
		protected void onPostExecute(NetKGmusicInfo result) {
			// TODO Auto-generated method stub
			if (updateview==false) {
				MainActivity.Update();
				updateview=true;
			}
			
			getlrc(result);
			super.onPostExecute(result);
		}
		
		
	}
	
	public  void getmusicinfo(NetKGmusicInfo info) {
		Getmusicinfo getmusicinfo=new Getmusicinfo(info);
		getmusicinfo.execute();
	}
	public void getlrc(NetKGmusicInfo info) {
		Getlrc getlrc=new Getlrc(info);
		getlrc.execute();
	}
	public void getlrcdownload(NetKGmusicInfo info) {
		Getlrcdownload getlrcdownload=new Getlrcdownload(info);
		getlrcdownload.execute();
	}
	public void getlink(NetKGmusicInfo info) {
		GetLinks getLinks=new GetLinks(info);
		getLinks.execute();
	}
}
