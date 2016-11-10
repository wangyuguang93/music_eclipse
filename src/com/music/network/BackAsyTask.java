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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class BackAsyTask extends AsyncTask<String, Void, Bitmap[]> {

	private String[] pic_small;
	private Bitmap[] bitmap;
	private boolean isfinish;
	private int size, jishu = -1;
	int i;
	// 获取音乐ico接口
	Getmusic_ico getmusic_ico = null;

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
		bitmap = new Bitmap[pic_small.length];

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
		String murl;
		InputStream inputStream = null;
		URL url = null;

		for (i = 0; i < pic_small.length; i++) {
			try {
				murl=pic_small[i];
				if (murl==null) {
					murl="http://45.76.107.227/mtw.jpg";
				}
				url = new URL(murl);
				Log.d("picUrl", pic_small[i]);
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
						bitmap[i] = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
						//return bitmap;

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // 获取输入流
				}
			}
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap[] result) {
		// TODO Auto-generated method stub

		getmusic_ico.getmusic_ico(bitmap);

		super.onPostExecute(result);
	}

}
