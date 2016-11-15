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

		Runnable runnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("图片下载线程1执行", "图片下载线程1执行");
				String murl;
				InputStream inputStream = null;
				URL url = null;
				int a;
				for (a = 0; a < pic_small.length/4; a++) {
					try {
						murl=pic_small[a];
						if (murl==null) {
							murl="http://45.76.107.227/mtw.jpg";
						}
						url = new URL(murl);
						Log.d("picUrl", pic_small[a]);
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
								bitmap[a] = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
								//return bitmap;

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} // 获取输入流
						}
					}
				}
				jishu++;
			}
		};
		Runnable runnable2 =new Runnable() {
			
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d("图片下载线程2执行", "图片下载线程2执行");
						String murl;
						InputStream inputStream = null;
						URL url = null;
						int b;
						for (b = pic_small.length/4; b < pic_small.length/2; b++) {
							try {
								murl=pic_small[b];
								if (murl==null) {
									murl="http://45.76.107.227/mtw.jpg";
								}
								url = new URL(murl);
								Log.d("picUrl", pic_small[b]);
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
										bitmap[b] = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
										//return bitmap;

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} // 获取输入流
								}
							}
						}
						jishu++;
					}
				};
		
		Runnable runnable3=new Runnable() {
			
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d("图片下载线程3执行", "图片下载线程3执行");
						String murl;
						int c;
						InputStream inputStream = null;
						URL url = null;
						for (c = pic_small.length/2; c < pic_small.length-pic_small.length/4; c++) {
							try {
								murl=pic_small[c];
								if (murl==null) {
									murl="http://45.76.107.227/mtw.jpg";
								}
								url = new URL(murl);
								Log.d("picUrl", pic_small[c]);
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
										bitmap[c] = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
										//return bitmap;

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} // 获取输入流
								}
							}
						}
						jishu++;
			}
		};
		Runnable runnable4=new Runnable() {
			
		
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d("图片下载线程4执行", "图片下载线程4执行");
						String murl;
						InputStream inputStream = null;
						URL url = null;
						int d;
						for (d = pic_small.length-pic_small.length/4; d < pic_small.length; d++) {
							try {
								murl=pic_small[d];
								if (murl==null) {
									murl="http://45.76.107.227/mtw.jpg";
								}
								url = new URL(murl);
								Log.d("picUrl", pic_small[d]);
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
										bitmap[d] = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
										//return bitmap;

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} // 获取输入流
								}
							}
						}
						jishu++;
						while(true){
						if (jishu==4) {
							Log.d("设置Bitmap", "设置Bitmap");
							getmusic_ico.getmusic_ico(bitmap);
							//MainActivity.Update();
							break;
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}

			}
		};
		thread=new Thread(runnable);
		thread2=new Thread(runnable2);
		thread3=new Thread(runnable3);
		thread4=new Thread(runnable4);
		
		thread.start();
		thread2.start();
		thread3.start();
		thread4.start();
		return bitmap;
		
	}

	@Override
	protected void onPostExecute(Bitmap[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
