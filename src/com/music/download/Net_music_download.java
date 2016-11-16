package com.music.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

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

public class Net_music_download extends AsyncTask<String, Integer, String>{
	private URL url;
	private String extname;
    final int DOWN_THREAD_NUM = 4;
    private Context context;
    String OUT_FILE_NAME="";  
    private int jishu=0;
    private Handler downloadfinish=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==1) {
				jishu++;
				if (jishu==4) {
					Toast.makeText(context, OUT_FILE_NAME+" 下载完成", Toast.LENGTH_SHORT).show();
					//Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					
					//MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath() + "/kgmusic/download/",OUT_FILE_NAME}, null, null);
					  MediaScannerConnection.scanFile(context,
			                    new String[] { Environment.getExternalStorageDirectory().getAbsolutePath() + "/kgmusic/download/"+OUT_FILE_NAME }, null,
			                    new MediaScannerConnection.OnScanCompletedListener() {
			                public void onScanCompleted(String path, Uri uri) {
			                    Log.i("ExternalStorage", "Scanned " + path + ":");
			                    Log.i("ExternalStorage", "-> uri=" + uri);
			                     Intent intent  = new Intent(); 
			    		        intent.putExtra("msg", "updatemusicdate");
			    		        intent.setAction("main");  
			    		        context.sendBroadcast(intent);  
			                }
			            });
				}
				
				
               // context.sendBroadcast(scanIntent);
			}
		}
    	
    };
    InputStream[] isArr = new InputStream[DOWN_THREAD_NUM];  
    RandomAccessFile[] outArr = new RandomAccessFile[DOWN_THREAD_NUM];  
    //构造函数
    public Net_music_download(Context context) {
		// TODO Auto-generated constructor stub
    	this.context=context;
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			extname=params[2];
			if (extname==null) {
				//String s1=mylujin[mymusicIndex];
				extname=params[0].substring(params[0].lastIndexOf('.')+1);
				
			}
			
			OUT_FILE_NAME=params[1]+"."+extname;
			url=new URL(params[0]);
			if (url!=null) {
				Log.d("下载链接", ""+url);
	            isArr[0] = url.openStream();  
	            long fileLen = getFileLength(url);  
	            System.out.println("网络资源的大小" + fileLen);  
	            // 以输出文件名创建第一个RandomAccessFile输出流  
	            //创建从中读取和向其中写入（可选）的随机存取文件流，第一个参数：文件名，第二个参数是：参数指定用以打开文件的访问模式  
	            //"rw"可能是可读可写，  
	           // outArr[0] = new RandomAccessFile(OUT_FILE_NAME, "rw"); 
	            
	          //下载目录
				File lujiu=Environment.getExternalStorageDirectory();
				File songdir=new File(lujiu+"/kgmusic/"+"/download/",OUT_FILE_NAME);
//				if (!songdir.exists()) {
//					songdir.mkdirs();
//					
//				}
				  // 每线程应该下载的字节数  
	            long numPerThred = fileLen / DOWN_THREAD_NUM;  
	            // 整个下载资源整除后剩下的余数取模  
	            long left = fileLen % DOWN_THREAD_NUM;
	            Log.d("songdir", ""+songdir);
	            outArr[0] = new RandomAccessFile(songdir, "rw");
	            // 创建一个与下载资源相同大小的空文件  
	            try {
//	            	 for (int i = 0; i < fileLen; i++) {  
//	 	                outArr[0].write(0);  
	            	outArr[0].setLength(fileLen);
	 	            
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
	           
	            
	            for (int i = 0; i < DOWN_THREAD_NUM; i++) {  
	                // 为每个线程打开一个输入流、一个RandomAccessFile对象，  
	                // 让每个线程分别负责下载资源的不同部分。  
	                //isArr[0]和outArr[0]已经使用，从不为0开始  
	                if (i != 0) {  
	                    // 以URL打开多个输入流  
	                    isArr[i] = url.openStream();  
	                    // 以指定输出文件创建多个RandomAccessFile对象  
	                    outArr[i] = new RandomAccessFile(songdir, "rw"); 	              	                  
	                }  
	                // 分别启动多个线程来下载网络资源  
	                if (i == DOWN_THREAD_NUM - 1) {  
	                    // 最后一个线程下载指定numPerThred+left个字节  
	                    new DownThread(i * numPerThred, (i + 1) * numPerThred  
	                            + left, isArr[i], outArr[i]).start();  
	                } else {  
	                    // 每个线程负责下载一定的numPerThred个字节  
	                    new DownThread(i * numPerThred, (i + 1) * numPerThred,  
	                            isArr[i], outArr[i]).start();  
	                }  
	            }  	
				
				
				
				
				
				
				
				
				
			}			
			
		} catch (Exception e) {
			// TODO: handle exception
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
    protected class DownThread extends Thread{
        // 定义字节数组（取水的竹筒）的长度  
        private final int BUFF_LEN = 32;  
          
        // 定义下载的起始点  
        private long start;  
          
        // 定义下载的结束点  
        private long end;  
          
        // 下载资源对应的输入流  
        private InputStream dis;  
          
        // 将下载到的字节输出到raf中  
        private RandomAccessFile draf;  
      
          
        // 构造器，传入输入流，输出流和下载起始点、结束点  
        public DownThread(long start, long end, InputStream is, RandomAccessFile raf) {  
            // 输出该线程负责下载的字节位置  
            System.out.println(start + "---->" + end);  
            this.start = start;  
            this.end = end;  
            dis = is;  
            draf = raf;  
        }  
      
        public void run() {  
            try {  
                dis.skip(start);  
                draf.seek(start);  
                // 定义读取输入流内容的的缓存数组（竹筒）  
                byte[] buff = new byte[BUFF_LEN];  
                // 本线程负责下载资源的大小  
                long contentLen = end - start;  
                // 定义最多需要读取几次就可以完成本线程的下载  
                long times = contentLen / BUFF_LEN + 4;  
                // 实际读取的字节数  
                int hasRead = 0;  
                for (int i = 0; i < times; i++) {  
                    hasRead = dis.read(buff);  
                    // 如果读取的字节数小于0，则退出循环！  
                    if (hasRead < 0) {  
                        break;  
                    }  
                    draf.write(buff, 0, hasRead); 
                  
                }  
              	Message message=new Message();
            	message.what=1;
            	downloadfinish.sendMessage(message);

            } catch (Exception ex) {  
                ex.printStackTrace();  
            }  
            // 使用finally块来关闭当前线程的输入流、输出流  
            finally {  
                try {  
                    if (dis != null) {  
                        dis.close(); 
                        
                    }  
                    if (draf != null) {  
                        draf.close();  
                    }  
                } catch (Exception ex) {  
                    ex.printStackTrace();  
                }  
            }  
        }  
    }
}
