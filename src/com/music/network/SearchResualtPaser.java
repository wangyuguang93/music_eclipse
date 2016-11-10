package com.music.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class SearchResualtPaser {
	static String TAG = "SearchResualtPaser";

	public static ArrayList <Network_Musicinfo> parse(InputStream xml) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(xml, "UTF-8");
		int eventType = parser.getEventType();
		ArrayList <Network_Musicinfo> infos = null;
		Network_Musicinfo info = null;
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					infos = new ArrayList <Network_Musicinfo>();
					break;
				case XmlPullParser.START_TAG:
					if("song_list".equals(parser.getName())){
						if(parser.getAttributeName(0).equals("false"))
							return null;
					}
					if("song".equals(parser.getName())){
						info = new Network_Musicinfo();
						break;
					}
					if(info != null){
						if("title".equals(parser.getName())){
							info.setMtitle(titleRefix(parser.nextText()));
							break;
						}
						if("song_id".equals(parser.getName())){
							info.setmSongId(parser.nextText()) ;
							break;
						}
						if("author".equals(parser.getName())){
							info.setMauthor(titleRefix(parser.nextText()));
							break;
						}
						
						if("album_title".equals(parser.getName())){
							info.setMalbum_title(titleRefix(parser.nextText()));
							break;
						}
						if("album_id".equals(parser.getName())){
							info.setMalbum_id(parser.nextText());
							break;
						}
//						if("pic_small".equals(parser.getName())){
//							info.setMpic_small(parser.nextText());
//							break;
//						}
						if("lrclink".equals(parser.getName())){
							String lrclink = parser.nextText();
							if((lrclink != null)&&
									(lrclink.substring(lrclink.lastIndexOf(".")+1).equals("lrc")))
								info.setMlrclink(lrclink);
							break;
						}
					
						
					}
				case XmlPullParser.END_TAG:
					if("song".equals(parser.getName())){
						try {
							getLinks(info, null);
						} catch (Exception e) {
							e.printStackTrace();
							info = null;
							break;
						} 
						infos.add(info);
						info = null;
					}
					break;
			}
			eventType = parser.next();
		}
		return infos;
	}
	
	static String titleRefix(String title){
		if(title.contains("<em>")){
			String temp = new String();
			String strings[] = title.split("<em>");
			for(String str:strings){
				temp += str;
			}
			title = temp;
		}
		if(title.contains("</em>")){
			String temp = new String();
			String strings[] = title.split("</em>");
			for(String str:strings){
				temp += str;
			}
			title = temp;
		}
		return title;
	}

	public static void getLinks(Network_Musicinfo info, String rate) throws Exception{
		String addr = "http://music.baidu.com/data/music/links?songIds=" + info.getmSongId();
//		if(rate != null){
//			addr += "&rate="+rate;
//		}
		Log.d("addr", addr);
		URL url = new URL(addr);
		InputStream ips = url.openConnection().getInputStream();
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=ips.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		ips.close();
		String jason = new String(outStream.toByteArray());
		JSONObject rootJson = new JSONObject(jason);
		String errorCode = rootJson.getString("errorCode");
		if(errorCode.equals("22000")){
			JSONObject data = rootJson.getJSONObject("data");
			JSONArray songList = data.getJSONArray("songList");
			JSONObject item = songList.getJSONObject(0);
//			String
			info.setMpic_small(fixLink(item.getString("songPicRadio")));
//			info.setSongPicBig(fixLink(item.getString("songPicBig")));
//			info.setSongPicRadio(fixLink(item.getString("songPicRadio")));
			info.setmMusicPath(fixLink(item.getString("songLink")));
			info.setmSize(item.getString("size"));
		}else{
			//Logger.e(TAG, "getLinks error errorCode=" + errorCode);
		}
	}

	private static String fixLink(String link){
		//"http://c.hiphotos.baidu.com/ting/pic/item/http://qukufile2.qianqian.com/data2/pic/115439298/115439298.jpg.jpg"
		if(link.contains("http://")){
			link = link.substring(link.lastIndexOf("http://"));
		}
		return link;
	}
}
