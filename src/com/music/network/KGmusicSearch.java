package com.music.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.base.Util.music_API;

import android.os.AsyncTask;
import android.util.Log;
public class KGmusicSearch implements music_API{
	private String mUrl = null;
	SearchThread mSearchThread = null;
	KGSeachCallback mCallback = null;
	
	public interface KGSeachCallback {
		public void onSearchResult(List<NetKGmusicInfo> resualt);
	}

	public void setKGCallBack(KGSeachCallback callbakc) {
		mCallback = callbakc;
	}

	private void onSearchResult(List<NetKGmusicInfo> resualt) {
		if (mCallback != null && !mSearchThread.mCancel) {
			mCallback.onSearchResult(resualt);
		}
	}
	
	public void search(String keys,int index) {
		try {
//			mUrl = musicapi1 + "&page_size=" + pageSize + "&page_no="
//					+ pageNo + "&query=";
			mUrl = Kgmusic;
			mUrl += URLEncoder.encode(keys, "UTF-8");
			mUrl+="&page="+index;
			Log.d("url", "search mUrl=" + mUrl);
			if (mSearchThread != null) {
				mSearchThread.mCancel = true;
			}
			mSearchThread = new SearchThread();
			//mSearchThread.start();
			mSearchThread.execute();
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
			

			
			//Log.d("xml", ""+inStream.toString());
			
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

private	class SearchThread extends AsyncTask<Void, Void, List<NetKGmusicInfo>> {
		boolean mCancel = false;

//		@Override
//		public void run() {
			

		@Override
		protected List<NetKGmusicInfo> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
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
			//	Log.d("test", "test");
				inStream.close();
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
		
			
			
			return null;
		}

		@Override
		protected void onPostExecute(List<NetKGmusicInfo> result) {
			// TODO Auto-generated method stub
			
			
			super.onPostExecute(result);
		}
		
		
	}
	
}
