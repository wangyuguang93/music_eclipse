package com.music.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import com.example.musicplay.MainActivity;
import com.music.download.GetAllLingk.DownLoadThread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class BackAsyTask extends AsyncTask<String, Void, Bitmap[]> {

	private String[] pic_small;
	private Bitmap[] bitmap;
	private boolean isfinish;
	private int size, jishu = 0;
	private int xianchengnum=5;//线程数
	Thread thread;
	Thread thread2;
	Thread thread3;
	Thread thread4;
	// 获取音乐ico接口
	Getmusic_ico getmusic_ico = null;
	public void setbitmap() {
	getmusic_ico.getmusic_ico(bitmap);
}
	public interface Getmusic_ico {
		public void getmusic_ico(Bitmap[] resualt);
	}

	public void setGetmusic(Getmusic_ico getmusic_ico) {

		this.getmusic_ico = getmusic_ico;
	}

	public BackAsyTask() {
		// TODO Auto-generated constructor stub
	}

	public BackAsyTask(String[] pic_small) {
		// TODO Auto-generated constructor stub
		this.pic_small = pic_small;
		try {
			bitmap = new Bitmap[pic_small.length];
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("设置bitmap出错", "设置bitmap出错");
			e.printStackTrace();
		}
		

	}

	public static class StreamTool {
		public static byte[] getBytes(InputStream is) throws Exception {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			is.close();
			bos.flush();
			byte[] result = bos.toByteArray();
			// System.out.println(new String(result));

			return result;
		}
	}

	@Override
	protected Bitmap[] doInBackground(String... params) {
		// TODO Auto-generated method stub

		Log.d("picUrl", params[0].toString());
		
		size=pic_small.length/20;
		
		if (pic_small.length>=20) {
			for (int i = 1; i <= xianchengnum; i++) {
				int start = (i - 1) * 4;
				int end = i * 4;
				for (int j = 0; j < size; j++) {
					if (j!=0) {
						start=(start+20);
						end=(end+20);
					}
					
				}
				new myrun(start, end).start();
				System.out.println("图片下载线程：" + i + ",下载：" + start + "--->" + end);
			
				
			}
		}else {
			new myrun(1, pic_small.length).start();
			System.out.println("图片下载线程：" + 1 + ",下载：" + 1 + "--->" + pic_small.length);
		}
		
		
		
		return bitmap;
		
	}

	@Override
	protected void onPostExecute(Bitmap[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	public class myrun extends Thread {
		private int start,end;
		public myrun(int start,int end) {
			// TODO Auto-generated constructor stub
			this.start=start;
			this.end=end;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
				String murl;
				InputStream inputStream = null;
				URL url = null;
				for (int i = start; i < end; i++) {
					

					try {
						murl=pic_small[i];
						if (murl==null||murl.equals("")) {
							murl="http://45.76.107.227/mtw.jpg";
						}
						url = new URL(murl);
						if (pic_small[i]!=null) {
							Log.d("picUrl", pic_small[i]);
						}
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // 服务器地址
					if (url != null) {
						// 打开连接
						HttpURLConnection httpURLConnection = null;
						try {
							httpURLConnection = (HttpURLConnection) url.openConnection();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						httpURLConnection.setConnectTimeout(3000);// 设置网络连接超时的时间为3秒
						try {
							httpURLConnection.setRequestMethod("GET");
						} catch (ProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // 设置请求方法为GET
						httpURLConnection.setDoInput(true); // 打开输入流
						int responseCode = 0;
						try {
							responseCode = httpURLConnection.getResponseCode();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // 获取服务器响应值
						if (responseCode == HttpURLConnection.HTTP_OK) { // 正常连接
							try {
								inputStream = httpURLConnection.getInputStream();

								byte[] imagebytes = null;
								try {
									imagebytes = StreamTool.getBytes(inputStream);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									bitmap[i] = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} // 获取输入流
						}
					}
				
					
					
					
				
			}
			bitmap();
			
		}
		
		
	}
	public void bitmap() {
		if (pic_small.length>=20) {
			jishu++;
			if (jishu==xianchengnum) {
				Log.d("设置Bitmap", "设置Bitmap");
				getmusic_ico.getmusic_ico(bitmap);
			}
		}else {
			Log.d("设置Bitmap", "设置Bitmap");
			getmusic_ico.getmusic_ico(bitmap);
		}
		
		
	}
}
