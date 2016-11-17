package com.example.musicplay;
import java.io.File;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

@SuppressLint("ServiceCast")
public class MyService extends Service implements OnCompletionListener, OnPreparedListener, OnBufferingUpdateListener                        {
	private final IBinder binder=new MusicBinder();
	private int mymusicIndex=0;
	private MediaPlayer mPlayer;
	public String danqian_length,length,gequ_num;//当前播放长度
	private TextView mytv_duration,mytv_currentposition,mytv_music_title,mytv_guqu_num;
	private SeekBar mypb_music_progress;
	private Context mycontext;
	private Handler mTimeHandler,myhandler;
	private String tile,playlujin;
	private String[] mylujin,mdata;
	private int id[];
	private long alm[];
	private int gbpb;
	private int max,dantime;
	private Boolean ismylast,isSave=false,issuoping=false;
	private BroadcastReceiver mbcr,suoping;
	private NotificationManager manager;
	private RemoteViews remoteViews;
	private Boolean isgengxitile,isSuopingTAG=false;
	private Notification notify;
	private ImageView myimg_ico;
	private WallpaperManager wallpaperManager;
	private Drawable drawable;
	private Bitmap bm;
	private Bitmap[] mbendi_tupian;
	public class MusicBinder extends Binder{
		MyService getService(){
			return MyService.this;
			
		}
		
	}
//public MyService() {
//	// TODO Auto-generated constructor stub
//}
//public MyService(File dir,String data[],int musicIndex,ImageButton ibPlayOrPuase) {
//	// TODO Auto-generated constructor stub
//	this.dir=dir;
//	this.ibPlayOrPuase=ibPlayOrPuase;
//	this.data=data;
//	this.musicIndex=musicIndex;
//}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
        mbcr = new MyBroadCastReceiver();  
        IntentFilter filter = new IntentFilter("guang93");  
        registerReceiver(mbcr, filter);  
        System.out.println("音乐服务被创建....");  
        
        
        
        //通知栏
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		

		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(isSave){
		SharedPreferences peizhi=getSharedPreferences("peizhi", Context.MODE_PRIVATE);
		Editor editor=peizhi.edit();
		editor.putString("lujin", playlujin);
		editor.putInt("max",max);
		editor.putString("tile", tile);
		editor.putInt("dantime", dantime);
		editor.putString("danqian_length", danqian_length);
		editor.putString("length", length);
		editor.putBoolean("ismylast", ismylast);
		editor.putInt("musicIndex", mymusicIndex);
		editor.putString("gequ_num", gequ_num);
		editor.commit();
		}
		
		if (mPlayer!=null) {
			mPlayer.pause();
			mPlayer.stop();
		}
		if (mbcr!=null) {
			unregisterReceiver(mbcr);
		}
		if (suoping!=null) {
			unregisterReceiver(suoping);
		}
		//取消通知栏
        if (remoteViews != null) {  
        	manager.cancel(100);
            Log.d("服务移除", "ok");
         
        }  
        //在锁屏时退出程序，还原壁纸
       if (issuoping) {  
            huanyuansuoping();  
        }  
		
	}
	

	public void play(String[] data,String lujin[],
			int musicIndex,TextView tv_duration,
			final TextView tv_currentposition,TextView tv_music_title,
			final SeekBar pb_music_progress,
			final Context context,Boolean islast,
			int seeto,
			final TextView tv_guqu_num,
			int musicid[],long almid[],
			ImageView img_ico,Bitmap[] bendi_tupian) {
		//mydir=dir;
		//mydata=data;
		mymusicIndex=musicIndex;
		mycontext=context;
		mytv_currentposition=tv_currentposition;
		mytv_duration=tv_duration;
		mytv_music_title=tv_music_title;
		mypb_music_progress=pb_music_progress;
		ismylast=islast;
		mytv_guqu_num=tv_guqu_num;
		mylujin=lujin;
		id=musicid;
		alm=almid;
		myimg_ico=img_ico;
		mdata=data;
		mbendi_tupian=bendi_tupian;
		if (bm==null) {
			wallpaperManager=(WallpaperManager) mycontext.getSystemService(Context.WALLPAPER_SERVICE);				
			drawable=wallpaperManager.getDrawable();
			bm = ((BitmapDrawable) drawable).getBitmap();
		}
		
		//final Timezh timezh=new Timezh();
		
		
		//Log.d("data", mydata[mymusicIndex].toString());
		if(mPlayer==null)
		{
			mPlayer=new MediaPlayer();
			
		}
		isgengxitile=false;
		
		playlujin=mylujin[mymusicIndex];
		//playlujin="http://yinyueshiting.baidu.com/data2/music/121023383/120996718252000128.mp3?xcode=e1febb2e6273d9c8e9c5304b21fec782";
		//Log.d("hhh", ""+playlujin);
		
		if (ismylast) {
			try{
			mPlayer.reset();
			//设置播放路径
			mPlayer.setDataSource(playlujin);
			Log.d("playlujin", playlujin);
			//Log.d("uri", playlujin);
			//mPlayer.prepare();
			mPlayer.prepareAsync();
			
			
			tile=mdata[mymusicIndex];
			mytv_music_title.setText("(正在缓冲...)"+tile);
			mPlayer.setOnPreparedListener(MyService.this);
			//mPlayer.start();
			mPlayer.setOnBufferingUpdateListener(this);
			seekTo(seeto);
			//jiemian();
			mPlayer.setOnCompletionListener(this);
			isSave=true;
			issuoping=true;
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		else {
			startplay(playlujin);
			ismylast=true;
			isSave=true;
			issuoping=true;
			mPlayer.setOnBufferingUpdateListener(this);
		}

		//初始化锁屏广播
		// Log.d("sp", "开始锁屏广播注册");
		if (issuoping) {
			if (suoping==null) {
				suoping=new SuopingReceiver();
		        IntentFilter sp = new IntentFilter();  
		        sp.addAction(Intent.ACTION_SCREEN_ON);  
		        sp.addAction(Intent.ACTION_SCREEN_OFF);  
		        sp.addAction(Intent.ACTION_USER_PRESENT);  
		        registerReceiver(suoping, sp);
		       // Log.d("sp", "锁屏广播注册");
			}
			
		}
		//更换壁纸

	}	
	
	public void startplay(String startlujin) {
		try{
			
			
			
			//mlujin
			//重置播放器
			
			mPlayer.reset();
			//设置播放路径
			Log.d("playlujin", playlujin);
			mPlayer.setDataSource(startlujin);
			//Log.d("playlujin", playlujin);
			Log.d("当前播放", ""+mymusicIndex);
			//缓冲
			//mPlayer.prepare();
			mPlayer.prepareAsync();
			tile=mdata[mymusicIndex];
			mytv_music_title.setText("(正在缓冲...)"+tile);
		//player=new MediaPlayer();
//			mPlayer.setOnPreparedListener(new OnPreparedListener() {
//				
//				@Override
//				public void onPrepared(MediaPlayer mp) {
//					// TODO Auto-generated method stub
//					mp.start();
//				}
//			});
//			mPlayer.setOnErrorListener(new OnErrorListener() {
//				
//				@Override
//				public boolean onError(MediaPlayer mp, int what, int extra) {
//					// TODO Auto-generated method stub
//					Log.d("错误", ""+what+" "+ ""+extra);
//					
//					return false;
//				}
//			});
			mPlayer.setOnPreparedListener(this);
			//更新界面
			
			//jiemian();
		
		        	mPlayer.setOnCompletionListener(this);
						
							
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
	}
	
	public void pause() {
		if(mPlayer!=null&&mPlayer.isPlaying()){
			mPlayer.pause();
		}
	}
	public void stop() {
		if(mPlayer!=null){
			mPlayer.stop();
			try{
				mPlayer.prepare();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public void jixuplay(){
		mPlayer.start();
	}
	
	
  //获取歌曲长度
        public int getMusicDuration()
        {
            int rtn = 0;
            if (mPlayer != null)
            {
                rtn = mPlayer.getDuration();
            }

            return rtn;
        }
  //是否在播放
        public Boolean isPlay() {
			// TODO Auto-generated method stub
        	Boolean isplay=mPlayer.isPlaying();
        	return isplay;
		}
        //获取当前播放进度
        public int getMusicCurrentPosition()
        {
            int rtn = 0;
            if (mPlayer != null)
            {
                rtn = mPlayer.getCurrentPosition();

            }

            return rtn;
        }

        public void seekTo(int position)
        {
            if (mPlayer != null)
            {
            	Log.d("跳转到：", ""+position);
            	mPlayer.seekTo(position);
            }
        }
        

        
        
        
        
        
        /**
         * 更新界面
         */
        public void jiemian() {
        	//更换壁纸
    		if (isSuopingTAG) {
    			Log.d("开始设置壁纸", "ok");
    			setsuoping();
    		}
        	if (mPlayer==null) {
				mPlayer=new MediaPlayer();
				
			}
        	int t1=mylujin.length;
        	String zongshu=""+t1;
        	int num=mymusicIndex;
        	gequ_num=""+(++num)+"/"+zongshu;
        	//Log.d("data", mydata[mymusicIndex].toString());
        	mytv_guqu_num.setText(gequ_num);
        	final Timezh timezh=new Timezh();
        	
        //音乐标题
//        	String s1=mylujin[mymusicIndex];
//        	final String s2=s1.substring(s1.lastIndexOf('/')+1);
        	tile=mdata[mymusicIndex];
			mytv_music_title.setText(tile);
//        	mytv_music_title.setText(mdata[mymusicIndex]);

			
			max = mPlayer.getDuration();			
			int ss3=timezh.ss(max);
			String ss2=null;
    		if (ss3<10) {
				ss2="0"+ss3;
			}
    		else{
    			ss2=""+ss3;
    		}
    		length=""+timezh.mm(max)+":"+ss2;
    		mytv_duration.setText(length);
    		tongzhi();

	        mTimeHandler = new Handler() {
	        	
	        	public void handleMessage(android.os.Message msg) { 
	        		
	        		if (msg.what == 0) {
	        			dantime=mPlayer.getCurrentPosition();
	        			mypb_music_progress.setProgress(dantime);
	        			int ss=timezh.ss(dantime);
	        			String ss1=null;
	            		if (ss<10) {
	    					ss1="0"+ss;
	    				}
	            		else{
	            			ss1=""+ss;
	            		}
	            		danqian_length=""+""+timezh.mm(dantime)+":"+ss1;
	            		mytv_currentposition.setText(danqian_length);
	        			sendEmptyMessageDelayed(0, 1000); 
	        			} 
	        		//通知栏

        		
	        		}
	        	}; //在你的onCreate的类似的方法里面启动这个Handler就可以了： mTimeHandler.sendEmptyMessageDelayed(0, 1000);
	        mTimeHandler.sendEmptyMessageDelayed(0, 1000);
			//更新listview
			Intent listv = new Intent();
			listv.putExtra("msg", "listview");			  
			listv.putExtra("zhi", mymusicIndex);
			listv.setAction("main");
			sendBroadcast(listv); 
	        	mypb_music_progress.setMax(max); 
	        	mypb_music_progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						gbpb=seekBar.getProgress();
						seekTo(gbpb);
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						dantime=progress;
	        			mypb_music_progress.setProgress(dantime);
	        			int ss=timezh.ss(dantime);
						String ss1=null;
	            		if (ss<10) {
	    					ss1="0"+ss;
	    				}
	            		else{
	            			ss1=""+ss;
	            		}
						danqian_length=""+""+timezh.mm(dantime)+":"+ss1;
	            		mytv_currentposition.setText(danqian_length);
	            		try {
	            			myimg_ico.setImageBitmap(mbendi_tupian[mymusicIndex]);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	            		
	            		
	            		
					}
				});
	        
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mymusicIndex++;
			
			if (mymusicIndex > mylujin.length - 1) {
				mymusicIndex = 0;
			}
			playlujin=mylujin[mymusicIndex];
			try{
			mp.reset();
		
			//设置播放路径
			mp.setDataSource(playlujin);
			//Log.d("当前播放", ""+this.musicIndex);
			//缓冲
			mp.prepareAsync();
			mp.setOnPreparedListener(this);
			mPlayer.setOnBufferingUpdateListener(this);
//			mp.prepare();
//			mp.setOnPreparedListener(new OnPreparedListener() {
//				
//				@Override
//				public void onPrepared(MediaPlayer mp) {
//					// TODO Auto-generated method stub
//					mp.start();
//				}
//			});
//			
			
			//更新界面
		
//			jiemian();
			
			//更新listview
			Intent listv = new Intent();
			listv.putExtra("msg", "listview");			  
			listv.putExtra("zhi", mymusicIndex);
			listv.setAction("main");
			sendBroadcast(listv); 
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
			}
		}
/**
 * 播放操作广播
 * @param
 *设置播放使用的广播
 */
		private class MyBroadCastReceiver extends BroadcastReceiver{  
			  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            // TODO Auto-generated method stub 
	        	String msg=intent.getStringExtra("msg");
	        	if (msg.equals("next")) {
	        		onCompletion(mPlayer);
	        		System.out.println("下一首");  
				}
	        	if (msg.equals("last")) {
	        		mymusicIndex--;
	        		mymusicIndex--;
	        		onCompletion(mPlayer);
	        		System.out.println("上一首");
				}
	        	if (msg.equals("pause")) {
	        		mPlayer.pause();
				}
	            //MethodService();  
	        	if (msg.equals("update_tongzhi")) {
	        		tongzhi();
				}
	        }
	          
	    }  		
		/**
		 * 锁屏广播类
		 * @param
		 * 锁屏广播类
		 */
		private class SuopingReceiver extends BroadcastReceiver {
			 private String action = null; 
			 
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				   // TODO Auto-generated method stub 
		        action = intent.getAction();  
		           if (Intent.ACTION_SCREEN_OFF.equals(action)) {  
		            // 锁屏  
		        	  Log.d("sp", "锁屏了"); 
		        	  isSuopingTAG=true;
		        //	  setsuoping();
		        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {  
		            // 解锁  
		        //	huanyuansuoping();
		        	 
		        	 
		        	 isSuopingTAG=false;
		        	 Log.d("sp", "开屏了");
		        }  
			}
			
		}
		
//通知栏方法
		/**
		 * 设置通知
		 */
		private void setNotification() {

			Notification.Builder builder = new Builder(this);
			Intent intent = new Intent(Intent.ACTION_MAIN);   
			intent.addCategory(Intent.CATEGORY_LAUNCHER);   
			intent.setClass(this, MainActivity.class);   
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  
			//以上inten能跳转回上一个activity
			
			//Intent intent = new Intent(this, MainActivity.class);
			// 点击跳转到主界面
			PendingIntent intent_go = PendingIntent.getActivity(this, 5, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.notice, intent_go);
			
			//退出
			Intent myexit = new Intent();
			myexit.putExtra("msg", "exit");
			myexit.setAction("main");  
			PendingIntent myexit_cole = PendingIntent.getBroadcast(this, 4,myexit,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.exit, myexit_cole);

			// 设置上一曲
			Intent last = new Intent();
			last.putExtra("msg", "last");
			last.setAction("guang93");  
			PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, last,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.last, intent_prev);

			// 设置播放
			if (mPlayer.isPlaying()) {
				Intent playorpause = new Intent();
				playorpause.putExtra("msg", "play");
				playorpause.setAction("main");  
				PendingIntent intent_play = PendingIntent.getBroadcast(this, 2,
						playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.play, intent_play);
			}
			if (!mPlayer.isPlaying()) {
				Intent playorpause = new Intent();
				playorpause.putExtra("msg", "play");
				playorpause.setAction("main");  
				PendingIntent intent_play = PendingIntent.getBroadcast(this, 6,
						playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.play, intent_play);
			}
					
			// 下一曲
			Intent next = new Intent();
			next.putExtra("msg", "next");
			next.setAction("guang93");  
			PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, next,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.next, intent_next);
			
			 builder.setSmallIcon(R.drawable.music_tile);
			 // 设置顶部图标  
			 notify = builder.build();  
		     notify.contentView = remoteViews; // 设置下拉图标  
		     notify.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport  
		     notify.flags = Notification.FLAG_ONGOING_EVENT; 
		     notify.icon = R.drawable.music_tile;  
		     manager.notify(100, notify); 
		     startForeground(100, notify);
		     /**
		      * test
		      */
		
		}
		
		//更新通知
		private void tongzhi() {
			// TODO Auto-generated method stub
			  Runnable tongzhilan=new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// TODO Auto-generated metho stub
						
																																	
						  remoteViews = new RemoteViews(getPackageName(),  
					                R.layout.customnotice);  
						  try {
							  remoteViews.setImageViewBitmap(R.id.widget_album, mbendi_tupian[mymusicIndex]);  
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					        
					        remoteViews.setTextViewText(R.id.title, tile);  
					       // remoteViews.setTextViewText(R.id.widget_artist, info.getArtist());  
					        if (mPlayer.isPlaying()) {  
					            remoteViews.setImageViewResource(R.id.play, R.drawable.pause);  
					        }else {  
					            remoteViews.setImageViewResource(R.id.play, R.drawable.play);  
					        } 	
					        setNotification();
					}	
};
		myhandler=new Handler();
		myhandler.post(tongzhilan);//设置通知栏
		
		}
		/**
		 * 设置锁屏壁纸
		 */
public void setsuoping () {
	  try {  
		 // Bitmap bitmap = MediaUtil.getArtwork(mycontext, id[mymusicIndex], alm[mymusicIndex], true, false);
	     //  wallpaperManager.setBitmap(mbendi_tupian[mymusicIndex]);  
	    } catch (Exception e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }  
	
}
/**
 * 还原锁屏壁纸
 */
public void huanyuansuoping() {
	try {  
        wallpaperManager.setBitmap(bm);  
    } catch (Exception e) {  
        // TODO Auto-generated catch block  
        e.printStackTrace();  
    } 
	
}

@Override
public void onPrepared(MediaPlayer mp) {
	// TODO Auto-generated method stub
	
	mp.start();
	try {
		jiemian();
	} catch (Exception e) {
		// TODO: handle exception
		Log.d("界面更新失败", "界面更新失败");
		e.printStackTrace();
	}
	
}

@Override
public void onBufferingUpdate(MediaPlayer mp, int percent) {
	// TODO Auto-generated method stub
	tile=mdata[mymusicIndex];
	if (percent<99) {
		mytv_music_title.setText("(正在缓冲"+percent+"%+...)"+tile);
	}else {
		mytv_music_title.setText(tile);
	}
	
	 System.out.println("缓冲了的百分比 : " + percent + " %");
	 mypb_music_progress.setSecondaryProgress(percent);
}
}
		
		



