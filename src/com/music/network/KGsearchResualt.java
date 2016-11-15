package com.music.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.base.Util.Base64Util;
import com.base.Util.MD5Utils;
import com.base.Util.music_API;

import android.os.Environment;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

public class KGsearchResualt implements music_API{
	private static String isgeci="恋人心88";
	private static Getmusic getmusic;
	static ArrayList<NetKGmusicInfo> infos;
	public static void setinfo() {
			infos=null;
			getmusic=null;
	}
	public static void setupdatelistview() {
		
		getmusic=null;
}
	
	public static ArrayList<NetKGmusicInfo> parse(InputStream Kg) throws Exception {
		if (infos==null) {
			infos = new ArrayList<NetKGmusicInfo>();
		}
		NetKGmusicInfo info = null;
		ByteArrayOutputStream outkg = new ByteArrayOutputStream();
		byte[] kgbuffer = new byte[1024];
		int len = 0;
		while ((len = Kg.read(kgbuffer)) != -1) {
			outkg.write(kgbuffer, 0, len);
		}
		String kgjason = new String(outkg.toByteArray());
		JSONObject kgrootJson = new JSONObject(kgjason);
		String total = kgrootJson.getJSONObject("data").getString("total");
		Log.d("total", "isgg");
		Log.d("total", total + "gg");
		if (!total.equals("0")) {
			JSONObject data = kgrootJson.getJSONObject("data");
			JSONArray netinfo = data.getJSONArray("info");
			for (int i = 0; i < netinfo.length(); i++) {
			JSONObject kgitem = netinfo.getJSONObject(i);			
				info=new NetKGmusicInfo();
				info.setFilename(kgitem.getString("filename"));
				info.setSongname(kgitem.getString("songname"));
				info.setM4afilesize(kgitem.getString("m4afilesize"));
				info.setHash320(kgitem.getString("320hash"));
				info.setMvhash(kgitem.getString("mvhash"));
				info.setFilesize128(kgitem.getString("filesize"));
				info.setOwnercount(kgitem.getString("ownercount"));
				info.setSqhash(kgitem.getString("sqhash"));
				info.setFilesize320(kgitem.getString("320filesize"));
				info.setDuration(kgitem.getString("duration"));
				info.setAlbum_id(kgitem.getString("album_id"));
				info.setHash(kgitem.getString("hash"));
				info.setSingername(kgitem.getString("singername"));
				info.setSqfilesize(kgitem.getString("sqfilesize"));
				info.setAlbum_name(kgitem.getString("album_name"));
//				
				if (getmusic==null) {
					getmusic=new Getmusic();
				}
				
				try {
					getmusic.getmusicinfo(info);
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("getmusic", "getmusic失败");
					e.printStackTrace();
				}
				
				
//				getmusic.getlink(info);
//				getmusic.getlrc(info);
//				getmusic.getlrcdownload(info);
				
				infos.add(info);
				info = null;
			}
		} else {
			Log.d("找不到没有歌曲", "找不到没有歌曲");
		}
		return infos;
	}
	public static void getMusinInfo (NetKGmusicInfo info) throws Exception {
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
			
				 
	}
	//获取歌词id和accesskey
	public static void getLcy(NetKGmusicInfo info) throws Exception {
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

						if (isgeci.equals(lcyitem.getString("song"))) {
						//	Log.d("歌名相同", "歌名相同");
							
						}else {
						//	Log.d("歌名不相同", "歌名不相同");
							lycDownload(info);
							isgeci=lcyitem.getString("song");
						}
				
				 }else{
				 //Logger.e(TAG, "getLinks error errorCode=" + errorCode);
				 }
				 

				 
	}
	
/**
* 下载歌词				 
*/
public static void lycDownload(NetKGmusicInfo info)throws Exception {
	 
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
				File lcydir=new File(lcylujiu+"/kgmusic/"+"/lcy/"+"/Cache/");
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
	 
	
}	
	public static void getLinks(NetKGmusicInfo info) throws Exception {
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
		
	}
	//该方法暂时无用
	private static String fixLink(String link) {
		// "http://c.hiphotos.baidu.com/ting/pic/item/http://qukufile2.qianqian.com/data2/pic/115439298/115439298.jpg.jpg"
		if (link.contains("http://")) {
			link = link.substring(link.lastIndexOf("http://"));
		}
		return link;
	}

}
