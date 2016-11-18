package com.music.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.example.musicplay.MainActivity;
import com.music.download.DemoLoader.DownLoadThread;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Net_music_download extends AsyncTask<String, Integer, String> {
	private URL url;
	private String extname;
	final int DOWN_THREAD_NUM = 5;
	private Context context;
	String OUT_FILE_NAME = "";
	private int jishu = 0;
	private int errorjishu=0;
	private File songdir;
	private Handler downloadfinish = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				finish();
			}
		}

	};
	
	InputStream[] isArr = new InputStream[DOWN_THREAD_NUM];

	// 构造函数
	public Net_music_download(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			// extname=params[2];
			// if (extname==null) {
			// String s1=mylujin[mymusicIndex];
			extname = params[0].substring(params[0].lastIndexOf('.') + 1);

			// }

			OUT_FILE_NAME = params[1] + "." + extname;
			url = new URL(params[0]);
			if (url != null) {
				Log.d("下载链接", "" + url);
				isArr[0] = url.openStream();
				long fileLen = getFileLength(url);
				System.out.println("网络资源的大小" + fileLen);


				// 下载目录
				File lujiu = Environment.getExternalStorageDirectory();
				songdir = new File(lujiu + "/kgmusic/" + "/download/", OUT_FILE_NAME);

				RandomAccessFile raf = new RandomAccessFile(songdir, "rwd");
				raf.setLength(fileLen);
				raf.close();
				// 假设三个线程下载
				long blockSize = fileLen / DOWN_THREAD_NUM;
				for (int threadId = 1; threadId <= DOWN_THREAD_NUM; threadId++) {
					long startIndex = (threadId - 1) * blockSize;
					long endIndex = threadId * blockSize - 1;
					if (threadId == DOWN_THREAD_NUM) {
						endIndex = fileLen;
					}
					System.out.println("线程：" + threadId + ",下载：" + startIndex + "--->" + endIndex);
					// 开始下载
					new DownLoadThread(threadId, startIndex, endIndex, url, songdir).start();
				}
				System.out.println("文件总长度为：" + fileLen);
			} else {
				System.out.println("请求失败！");
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	// 定义获取指定网络资源的长度的方法
	public static long getFileLength(URL url) throws Exception {
		long length = 0;
		// 打开该URL对应的URLConnection
		URLConnection con = url.openConnection();
		// 获取连接URL资源的长度
		long size = con.getContentLength();
		length = size;
		return length;
	}

	public class DownLoadThread extends Thread {
		private long threadId;
		private long startIndex;
		private long endIndex;
		private URL murl;
		private File dlFile;

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
		public DownLoadThread(long threadId, long startIndex2, long endIndex2, URL murl, File songdir) {
			super();
			this.threadId = threadId;
			this.startIndex = startIndex2;
			this.endIndex = endIndex2;
			this.murl = murl;
			dlFile = songdir;
		}

		@Override
		public void run() {
			super.run();
			// URL url;
			// try {
			// url = new URL(path);
			try {
				HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
				// 请求服务器下载部分的文件，制定开始的位置，和结束位置
				connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
				connection.setDoInput(true);
				connection.setRequestMethod("GET");
				connection.setReadTimeout(5000);
				// 从服务器获取的全部数据，返回：200，从服务器获取部分数据，返回：206
				int code = connection.getResponseCode();
				System.out.println("code = " + code);
				InputStream is = connection.getInputStream();
				RandomAccessFile raf = new RandomAccessFile(dlFile, "rwd");
				// 随机写文件的时候，从什么时候开始
				raf.seek(startIndex);
				int len = 0;
				byte[] buff = new byte[1024];
				while ((len = is.read(buff)) != -1) {
					raf.write(buff, 0, len);

				}
				Message message = new Message();
				message.what = 1;
				downloadfinish.sendMessage(message);
				
				is.close();
				raf.close();
				System.out.println("歌曲下载线程：" + threadId + ",下载完成");
			} catch (Exception e) {
				errorjishu++;
				Message message = new Message();
				message.what = 1;
				downloadfinish.sendMessage(message);
				e.printStackTrace();
			}
		}
	}
	/**
	 * 下载情况
	 */
	public void finish() {

		jishu++;
		if (jishu == DOWN_THREAD_NUM&&errorjishu==0) {
			Toast.makeText(context, OUT_FILE_NAME + " 下载完成", Toast.LENGTH_SHORT).show();
			// Intent scanIntent = new
			// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

			// MediaScannerConnection.scanFile(context, new
			// String[]{Environment.getExternalStorageDirectory().getAbsolutePath()
			// + "/kgmusic/download/",OUT_FILE_NAME}, null, null);
			MediaScannerConnection.scanFile(context,
					new String[] { Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/kgmusic/download/" + OUT_FILE_NAME },
					null, new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
							Log.i("ExternalStorage", "Scanned " + path + ":");
							Log.i("ExternalStorage", "-> uri=" + uri);
							Intent intent = new Intent();
							intent.putExtra("msg", "updatemusicdate");
							intent.setAction("main");
							context.sendBroadcast(intent);
						}
					});
		}
		if (jishu==DOWN_THREAD_NUM&&errorjishu!=0) {
			Toast.makeText(context, OUT_FILE_NAME + " 下载失败", Toast.LENGTH_SHORT).show();
			MainActivity.delete(songdir);
		}
		// context.sendBroadcast(scanIntent);
	
	}
	
}
