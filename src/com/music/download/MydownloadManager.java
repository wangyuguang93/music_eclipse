package com.music.download;

import java.io.File;
import java.net.PortUnreachableException;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MydownloadManager {
	private String url;
	private String filename;
	private DownloadManager manager;
	private Context context;
	DownloadManager.Request musicrequest;
	private  BroadcastReceiver receiver;
	public MydownloadManager(Context context,String url,String filename) {
		// TODO Auto-generated constructor stub
		this.filename=filename;
		this.url=url;
		this.context=context;
	}
	public void downloadmusic() {
		/**
	     * 该方法是调用了系统的下载管理器
	     */
	   
	        /**
	         * 在这里返回的 reference 变量是系统为当前的下载请求分配的一个唯一的ID，
	         * 我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操作
	         * 或者查询下载的状态以及取消下载等等
	         */
	        Uri uri = Uri.parse(url);        //下载连接
	        manager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);  //得到系统的下载管理
	     // 下载目录
	        final String extname=url.substring(url.lastIndexOf('.') + 1);
	        
	        String lujiu = Environment.getExternalStorageDirectory()+"/kgmusic/" + "/download/";
			File songdir = new File(lujiu + "/kgmusic/" + "/download/", filename+"."+extname);
	        
			musicrequest = new DownloadManager.Request(uri);  //得到连接请求对象
//			musicrequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);   //指定在什么网络下进行下载，这里我指定了WIFI网络
			musicrequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,filename+"."+extname);  //制定下载文件的保存路径，我这里保存到根目录
		
			musicrequest.setVisibleInDownloadsUi(true);  //设置显示下载界面
			musicrequest.allowScanningByMediaScanner();  //表示允许MediaScanner扫描到这个文件，默认不允许。
			musicrequest.setTitle("正在下载："+filename);      //设置下载中通知栏的提示消息
			musicrequest.setDescription("正在下载："+filename);//设置设置下载中通知栏提示的介绍
	        final long downLoadId = manager.enqueue(musicrequest);               //启动下载,该方法返回系统为当前下载请求分配的一个唯一的ID
	        //注册广播接收器
	        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
	          receiver = new BroadcastReceiver() {
	         
	             public void onReceive(final Context context, Intent intent) {
	                 long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
	                 if (downLoadId == myDwonloadID) {
	                	 Toast.makeText(context, filename+"."+extname+" 下载完成", Toast.LENGTH_SHORT).show();
	                	 MediaScannerConnection.scanFile(context,
	 							new String[] { Environment.getExternalStorageDirectory().getAbsolutePath()
	 									+ "/kgmusic/download/" + filename+"."+extname },
	 							null, new MediaScannerConnection.OnScanCompletedListener() {
	 								public void onScanCompleted(String path, Uri uri) {
	 									Log.i("ExternalStorage", "Scanned " + path + ":");
	 									Log.i("ExternalStorage", "-> uri=" + uri);
	 									Intent intent1 = new Intent();
	 									intent1.putExtra("msg", "updatemusicdate");
	 									intent1.setAction("main");
	 									context.sendBroadcast(intent1);
	 									unregisterReceiver();
	 								}
	 							});
	                     }
	                 }
	             };
	         context.registerReceiver(receiver, filter);
		
	}
	public void unregisterReceiver() {
		context.unregisterReceiver(receiver);
	}
}
