package com.music.network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;


public class KGsearchResualt {

	public static ArrayList <NetKGmusicInfo> parse(InputStream Kg) throws Exception{
		
		ArrayList <NetKGmusicInfo> infos = null;
		NetKGmusicInfo info = null;
		ByteArrayOutputStream outkg = new ByteArrayOutputStream();
		byte[] kgbuffer = new byte[1024];
		int len = 0;
		while( (len=Kg.read(kgbuffer)) != -1 ){
			outkg.write(kgbuffer, 0, len);
		}
		String kgjason = new String(outkg.toByteArray());
		JSONObject kgrootJson = new JSONObject(kgjason);
		String total = kgrootJson.getJSONObject("data").getString("total");
		Log.d("total", "isgg");
		Log.d("total", total+"gg");
		if (!total.equals("0")) {
			JSONObject data = kgrootJson.getJSONObject("data");
			JSONArray netinfo = data.getJSONArray("info");
			JSONObject kgitem = netinfo.getJSONObject(0);
			info.setFilename(kgitem.getString("filename"));
			info.setSongname(kgitem.getString("songname"));
			info.setM4afilesize(kgitem.getString("m4afilesize"));
			
			info.setHash320(kgitem.getString("320hash"));
			info.setMvhash(kgitem.getString("mvhash"));
			info.setFileSize(kgitem.getString("filesize"));
			info.setOwnercount(kgitem.getString("ownercount"));
			info.setSqhash(kgitem.getString("sqhash"));
			info.setFilesize320(kgitem.getString("320filesize"));
			info.setDuration(kgitem.getString("duration"));
			info.setAlbum_id(kgitem.getString("album_id"));
			info.setHash(kgitem.getString("hash"));
			info.setSingername(kgitem.getString("singername"));
			info.setSqfilesize(kgitem.getString("sqfilesize"));
		}
		else {
			Log.d("找不到没有歌曲", "找不到没有歌曲");
		}
		return infos;
	}
	
	public static void getLinks(NetKGmusicInfo info) throws Exception{
//		String addr = "http://music.baidu.com/data/music/links?songIds=" + info.getmSongId();
//
//		Log.d("addr", addr);
//		URL url = new URL(addr);
//		InputStream ips = url.openConnection().getInputStream();
//		
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024];
//		int len = 0;
//		while( (len=ips.read(buffer)) != -1 ){
//			outStream.write(buffer, 0, len);
//		}
//		ips.close();
//		String jason = new String(outStream.toByteArray());
//		JSONObject rootJson = new JSONObject(jason);
//		String errorCode = rootJson.getString("errorCode");
//		if(errorCode.equals("22000")){
//			JSONObject data = rootJson.getJSONObject("data");
//			JSONArray songList = data.getJSONArray("songList");
//			JSONObject item = songList.getJSONObject(0);
////			String
//			info.setMpic_small(fixLink(item.getString("songPicRadio")));
////			info.setSongPicBig(fixLink(item.getString("songPicBig")));
////			info.setSongPicRadio(fixLink(item.getString("songPicRadio")));
//			info.setmMusicPath(fixLink(item.getString("songLink")));
//			info.setmSize(item.getString("size"));
//		}else{
//			//Logger.e(TAG, "getLinks error errorCode=" + errorCode);
//		}
	}

	private static String fixLink(String link){
		//"http://c.hiphotos.baidu.com/ting/pic/item/http://qukufile2.qianqian.com/data2/pic/115439298/115439298.jpg.jpg"
		if(link.contains("http://")){
			link = link.substring(link.lastIndexOf("http://"));
		}
		return link;
	}
}
