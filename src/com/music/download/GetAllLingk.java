package com.music.download;

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
import com.example.musicplay.MainActivity;
import com.music.network.NetKGmusicInfo;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class GetAllLingk extends AsyncTask<String, Integer, String> implements music_API {
	private ArrayList<NetKGmusicInfo> infos;
	private NetKGmusicInfo[] info;
	private int num = 5;// 线程数
	private int jishu = 0;
	private int gecijishu = 0;
	private int downloadjishu = 0;
	private int size=0;

	public GetAllLingk(ArrayList<NetKGmusicInfo> infos) {
		// TODO Auto-generated constructor stub
		this.infos = infos;
	}

	@Override
	protected String doInBackground(String... params) {
		info = infos.toArray(new NetKGmusicInfo[infos.size()]);
		size=info.length/20;
		
		//num = 5*size/20;// 线程数
		
		for (int i = 1; i <= num; i++) {
			int start = (i - 1) * 4;
			int end = i * 4;
		for (int j = 0; j < size; j++) {
			if (j!=0) {
				start=start+20;
				end=end+20;
			}
		}
			new DownLoadThread(start, end, info,i).start();
			
			System.out.println("线程：" + i + ",下载：" + start + "--->" + end);
			
		}
		
		

		
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 下载歌曲信息
	 * 
	 * @author guang
	 *
	 */
	public class DownLoadThread extends Thread {
		private NetKGmusicInfo[] dinfo;
		private int start;
		private int end;
		private int threadId;

		/**
		 * 
		 * @param threadId
		 *            线程id
		 * @param startIndex2
		 *            线程下载开始位置
		 * @param endIndex2
		 *            线程下载结束位置
		 * @param string
		 *            线程下载结束文件放置地址
		 */
		public DownLoadThread(int start, int end, NetKGmusicInfo[] dinfo,int threadId) {
			super();
			this.dinfo = dinfo;
			this.start = start;
			this.end = end;
			this.threadId=threadId;
		}

		@Override
		public void run() {
			super.run();
			
				for (int j = start; j < end; j++) {
			//		Log.d("j", "" + start);
			//		Log.d("j", "" + end);
					
					try {
						String addr = KgmusicInfo + info[j].getHash();
						// Log.d("addr", addr);
						URL url = new URL(addr);
						InputStream kgmusicinfo = url.openConnection().getInputStream();

						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = kgmusicinfo.read(buffer)) != -1) {
							outStream.write(buffer, 0, len);
						}
						kgmusicinfo.close();

						String jason = new String(outStream.toByteArray());
						JSONObject rootJson = new JSONObject(jason);
						String status = rootJson.getString("status");
						if (status.equals("1")) {
							info[j].setImgUrl(rootJson.getString("imgUrl"));
							info[j].setUrl128(rootJson.getString("url"));
							info[j].setReq_hash(rootJson.getString("req_hash"));
							
							
							
						} else {
							// Logger.e(TAG, "getLinks error errorCode=" +
							// errorCode);
							
						}

						System.out.println("歌曲信息：" + url + ",下载完成");
					} catch (Exception e) {
						e.printStackTrace();
					}
				
			}
			System.out.println("歌曲下载线程：" + threadId + ",下载完成");
			
			getGlink();
		}
	}

	/**
	 * 下载歌曲链接方法
	 */
	public void getGlink() {
		jishu++;
		if (jishu == num) {
			// 下载歌曲链接
			
			for (int i = 1; i <= num; i++) {
				int start = (i - 1) * 4;
				int end = i * 4;
				new Getlink(start, end, info).start();

			}

		}
	}

	/**
	 * 下载链接类
	 * 
	 * @author guang
	 *
	 */
	public class Getlink extends Thread {
		private NetKGmusicInfo[] linfo;
		private int start;
		private int end;

		/**
		 * 
		 * @param threadId
		 *            线程id
		 * @param startIndex2
		 *            线程下载开始位置
		 * @param endIndex2
		 *            线程下载结束位置
		 * @param string
		 *            线程下载结束文件放置地址
		 */
		public Getlink(int start, int end, NetKGmusicInfo[] linfo) {
			super();
			this.linfo = linfo;
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			super.run();
		
				for (int j = start; j < end; j++) {
					try {

						String hash320 = linfo[j].getHash320();
						String hashsq = linfo[j].getSqhash();
						String key320 = MD5Utils.getMD5(hash320 + "kgcloud");
						String keysq = MD5Utils.getMD5(hashsq + "kgcloud");
						String addr320 = KgmusicDownload + "&hash=" + hash320 + "&key=" + key320;
						String addrsp = KgmusicDownload + "&hash=" + hashsq + "&key=" + keysq;
						// Log.d("addr", addr320);
						URL url320 = new URL(addr320);
						URL urlsq = new URL(addrsp);
						// 获取320链接
						InputStream ips320 = url320.openConnection().getInputStream();

						ByteArrayOutputStream outStream320 = new ByteArrayOutputStream();
						byte[] buffer320 = new byte[1024];
						int len320 = 0;
						while ((len320 = ips320.read(buffer320)) != -1) {
							outStream320.write(buffer320, 0, len320);
						}
						ips320.close();
						String jason320 = new String(outStream320.toByteArray());
						JSONObject rootJson320 = new JSONObject(jason320);
						String status320 = rootJson320.getString("status");

						if (status320.equals("1")) {
							linfo[j].setUrl320(rootJson320.getString("url"));
							linfo[j].setExtName(rootJson320.getString("extName"));
						} else {
							// Logger.e(TAG, "getLinks error errorCode=" +
							// errorCode);
						}
						// 获取无损链接
						InputStream ipssq = urlsq.openConnection().getInputStream();

						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						byte[] buffersq = new byte[1024];
						int lensq = 0;
						while ((lensq = ipssq.read(buffersq)) != -1) {
							outStream.write(buffersq, 0, lensq);
						}
						ipssq.close();
						String jasonsq = new String(outStream.toByteArray());
						JSONObject rootJsonsq = new JSONObject(jasonsq);
						String statussq = rootJsonsq.getString("status");

						if (statussq.equals("1")) {
							linfo[j].setUrlsq(rootJsonsq.getString("url"));
							linfo[j].setExtNamesq(rootJsonsq.getString("extName"));
						} else {
							// Logger.e(TAG, "getLinks error errorCode=" +
							// errorCode);
						}
						System.out.println("链接下载：" + addr320 + ",下载完成");
						hash320 = null;
						hashsq = null;
						key320 = null;
						keysq = null;
						addr320 = null;
						addrsp = null;
						url320 = null;
						urlsq = null;
						ips320 = null;
						ipssq = null;
						jasonsq = null;
						jason320 = null;
						rootJson320 = null;
						rootJsonsq = null;
						status320 = null;
						statussq = null;
						buffer320 = null;
						buffersq = null;

						// System.out.println("歌曲链接：" + url320[j] + ",下载完成");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				
			}
			getLrc();
		}
	}

	/**
	 * 获取歌词链接方法
	 */
	public void getLrc() {
		gecijishu++;
		if (gecijishu == num) {
			//
			MainActivity.Update();
			for (int i = 1; i <= num; i++) {
				int start = (i - 1) * 4;
				int end = i * 4;
				new GetLrc(start, end, info).start();

			}
		}
	}

	/**
	 * 获取下载歌词类
	 * 
	 * @author guang
	 *
	 */
	public class GetLrc extends Thread {
		private NetKGmusicInfo[] ylinfo;
		private int start;
		private int end;

		/**
		 * 
		 * @param threadId
		 *            线程id
		 * @param startIndex2
		 *            线程下载开始位置
		 * @param endIndex2
		 *            线程下载结束位置
		 * @param string
		 *            线程下载结束文件放置地址
		 */
		public GetLrc(int start, int end, NetKGmusicInfo[] ylinfo) {
			super();
			this.ylinfo = ylinfo;
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			super.run();
			
				for (int j = start; j < end; j++) {
					try {

						String slcyurl = Kglyc;
						slcyurl += URLEncoder.encode(ylinfo[j].getSongname(), "UTF-8");

						slcyurl += "&duration=" + ylinfo[j].getDuration() + "000";

						URL lcyurl = new URL(slcyurl);
						InputStream kglcy = lcyurl.openConnection().getInputStream();

						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						byte[] bufferlcy = new byte[1024];
						int lenlcy = 0;
						while ((lenlcy = kglcy.read(bufferlcy)) != -1) {
							outStream.write(bufferlcy, 0, lenlcy);
						}
						kglcy.close();

						String lcyjason = new String(outStream.toByteArray());
						JSONObject lcyrootJson = new JSONObject(lcyjason);
						String lcyinfo = lcyrootJson.getString("info");
						if (lcyinfo.equals("OK")) {
							// JSONObject data =
							// lcyrootJson.getJSONObject("data");
							JSONArray geciinfo = lcyrootJson.getJSONArray("candidates");
							JSONObject lcyitem = geciinfo.getJSONObject(0);

							ylinfo[j].setAccesskey(lcyitem.getString("accesskey"));
							ylinfo[j].setLycid(lcyitem.getString("id"));

							// if (isgeci.equals(lcyitem.getString("song"))) {
							// // Log.d("歌名相同", "歌名相同");
							//
							// }else {
							// // Log.d("歌名不相同", "歌名不相同");
							// lycDownload(info);
							// isgeci=lcyitem.getString("song");
							// }
							//
						} else {
							// Logger.e(TAG, "getLinks error errorCode=" +
							// errorCode);
						}

						System.out.println("歌词链接下载：" + slcyurl + ",下载完成");
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				
			}
			getdownload();
		}
	}

	/**
	 * 下载歌词方法
	 */
	public void getdownload() {
		downloadjishu++;
		if (downloadjishu == num) {
			for (int i = 1; i <= num; i++) {
				int start = (i - 1) * 4;
				int end = i * 4;
				new Getdownloadlrc(start, end, info).start();

			}
		}
	}

	/**
	 * 下载歌词类
	 * 
	 * @author guang
	 *
	 */
	public class Getdownloadlrc extends Thread {
		private NetKGmusicInfo[] dylinfo;
		private int start;
		private int end;

		/**
		 * 
		 * @param threadId
		 *            线程id
		 * @param startIndex2
		 *            线程下载开始位置
		 * @param endIndex2
		 *            线程下载结束位置
		 * @param string
		 *            线程下载结束文件放置地址
		 */
		public Getdownloadlrc(int start, int end, NetKGmusicInfo[] dylinfo) {
			super();
			this.dylinfo = dylinfo;
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			super.run();
			
				for (int j = start; j < end; j++) {
					try {
						
						String lcydownload = Kglycdownload + "&id=" + dylinfo[j].getLycid() + "&&accesskey="
								+ dylinfo[j].getAccesskey();
						Log.d("下载歌词文件", lcydownload);
						URL downloadlcy = new URL(lcydownload);
						InputStream lcyinputStream = downloadlcy.openConnection().getInputStream();

						ByteArrayOutputStream lcyout = new ByteArrayOutputStream();
						byte[] bufferlcydownload = new byte[1024];
						int lenlcydownload = 0;
						while ((lenlcydownload = lcyinputStream.read(bufferlcydownload)) != -1) {
							lcyout.write(bufferlcydownload, 0, lenlcydownload);
						}
						lcyinputStream.close();

						String lcyjasondoload = new String(lcyout.toByteArray());
						JSONObject lcyrootJsondownload = new JSONObject(lcyjasondoload);
						String lcyinfodownload = lcyrootJsondownload.getString("info");
						if (lcyinfodownload.equals("OK")) {
							String geci = (lcyrootJsondownload.getString("content"));

							dylinfo[j].setFmt(lcyrootJsondownload.getString("fmt"));
							byte[] lcy = Base64Util.decode(geci);
							String geciname = dylinfo[j].getSongname() + "." + dylinfo[j].getFmt();
							// 歌词缓存目录
							File lcylujiu = Environment.getExternalStorageDirectory();
							File lcydir = new File(lcylujiu + "/kgmusic/" + "/lrc/" + "/Cache/");
							if (!lcydir.exists()) {
								lcydir.mkdirs();

							}

							File lcygeci = new File(lcydir, geciname);
							FileOutputStream geciout = new FileOutputStream(lcygeci);
							geciout.write(lcy);
							geciout.close();
							geci = null;
							lcylujiu = null;
							lcydir = null;
							geciout = null;
							lcygeci = null;
							downloadlcy = null;
							bufferlcydownload = null;
							lcyrootJsondownload = null;
							lcyinputStream = null;

						} else {
							// Logger.e(TAG, "getLinks error errorCode=" +
							// errorCode);
						}

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			
		}
	}

}
