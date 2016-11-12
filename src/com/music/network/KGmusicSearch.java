package com.music.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.base.Util.music_API;

import android.util.Log;
public class KGmusicSearch implements music_API{
	private String mUrl = null;
	SearchThread mSearchThread = null;
	SeachCallback mCallback = null;
	
	public interface SeachCallback {
		public void onSearchResult(List<NetKGmusicInfo> resualt);
	}

	public void setCallBack(SeachCallback callbakc) {
		mCallback = callbakc;
	}

	private void onSearchResult(List<NetKGmusicInfo> resualt) {
		if (mCallback != null && !mSearchThread.mCancel) {
			mCallback.onSearchResult(resualt);
		}
	}
	
	public void search(String keys) {
		try {
//			mUrl = musicapi1 + "&page_size=" + pageSize + "&page_no="
//					+ pageNo + "&query=";
			mUrl = Kgmusic;
			mUrl += URLEncoder.encode(keys, "UTF-8");
			Log.d("url", "search mUrl=" + mUrl);
			if (mSearchThread != null) {
				mSearchThread.mCancel = true;
			}
			mSearchThread = new SearchThread();
			mSearchThread.start();
		} catch (UnsupportedEncodingException e) {
			//onSearchResult(null);
			e.printStackTrace();
		}
	}

	public List<NetKGmusicInfo> searchSync(String keys) {
		InputStream inStream = null;
		List<NetKGmusicInfo> resualt = null;
		try {
			mUrl = musicapi1;
			
			Log.d("keys", ""+keys);
			mUrl += URLEncoder.encode(keys, "UTF-8");
			//Logger.i(TAG, "search mUrl=" + mUrl);
			URL url = new URL(mUrl);
			Log.d("url", ""+url);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			

			
			Log.d("xml", ""+inStream.toString());
			
			resualt = KGsearchResualt.parse(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resualt;
	}
	
	public void cancel() {
		if (mSearchThread != null) {
			mSearchThread.mCancel = true;
		}
	}

	class SearchThread extends Thread {
		boolean mCancel = false;

		@Override
		public void run() {
			InputStream inStream = null;
			try {
				URL url = new URL(mUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				inStream = conn.getInputStream();
				List<NetKGmusicInfo> resualt = KGsearchResualt.parse(inStream);
				onSearchResult(resualt);
			} catch (Exception e) {
				//onSearchResult(null);
				e.printStackTrace();
			} finally {
				try {
					if (inStream != null) {
						inStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
