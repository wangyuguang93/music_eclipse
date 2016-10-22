package com.example.musicplay;
import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MyService extends Service{
	private final IBinder binder=new MusicBinder();
	private File mydir;
	private String mydata[];
	private int mymusicIndex;
	private ImageButton myibPlayOrPuase;
	private MediaPlayer mPlayer;
	public static int length;//播放长度
	public static int danqian_length;//当前播放长度
	private TextView mytv_duration,mytv_currentposition,mytv_music_title;
	private SeekBar mypb_music_progress;
	private Context mycontext;
	private Handler mTimeHandler;
	private String lujin;
	private int gbpb;
	
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
		mPlayer.stop();
	}
	public void play(File dir,String data[],int musicIndex,TextView tv_duration,final TextView tv_currentposition,TextView tv_music_title,final SeekBar pb_music_progress,final Context context) {
		mydir=dir;
		mydata=data;
		mymusicIndex=musicIndex;
		mycontext=context;
		mytv_currentposition=tv_currentposition;
		mytv_duration=tv_duration;
		mytv_music_title=tv_music_title;
		mypb_music_progress=pb_music_progress;
		//final Timezh timezh=new Timezh();
		
		
		//Log.d("data", mydata[mymusicIndex].toString());
		if(mPlayer==null)
		{
			mPlayer=new MediaPlayer();
			
		}
		
			try{
				
			
				lujin=dir.getAbsolutePath()+"/"+mydata[mymusicIndex];
				//mlujin
				//重置播放器
				
				mPlayer.reset();
				//设置播放路径
				mPlayer.setDataSource(dir.getAbsolutePath()+"/"+mydata[mymusicIndex]);
				
				Log.d("当前播放", ""+mymusicIndex);
				//缓冲
				mPlayer.prepare();
			//player=new MediaPlayer();
				mPlayer.setOnPreparedListener(new OnPreparedListener() {
					
					@Override
					public void onPrepared(MediaPlayer mp) {
						// TODO Auto-generated method stub
						mp.start();
					}
				});
				mPlayer.setOnErrorListener(new OnErrorListener() {
					
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						// TODO Auto-generated method stub
						Log.d("错误", ""+what+" "+ ""+extra);
						
						return false;
					}
				});
				
				//更新界面
				
				jiemian();
			
			        	mPlayer.setOnCompletionListener(new OnCompletionListener() {
							
							@Override
							public void onCompletion(MediaPlayer mp) {
								// TODO Auto-generated method stub
								mymusicIndex++;
								
								if (mymusicIndex > mydata.length - 1) {
									mymusicIndex = 0;
								}
								try{
								mp.reset();
							
								//设置播放路径
								mp.setDataSource(mydir.getAbsolutePath()+"/"+mydata[mymusicIndex]);
								//Log.d("当前播放", ""+this.musicIndex);
								//缓冲
								mp.prepare();
								mp.setOnPreparedListener(new OnPreparedListener() {
									
									@Override
									public void onPrepared(MediaPlayer mp) {
										// TODO Auto-generated method stub
										mp.start();
									}
								});
								

								//更新界面
							
								jiemian();
								
								}catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
									
								}
							}
						});
								
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
            	mPlayer.seekTo(position);
            }
        }
        public void jiemian() {
        	Log.d("data", mydata[mymusicIndex].toString());
        	final Timezh timezh=new Timezh();
			mytv_music_title.setText("正在播放："+mydata[mymusicIndex].toString());
			
			int max = mPlayer.getDuration();
			mypb_music_progress.setMax(max);
			int ss3=timezh.ss(max);
			String ss2=null;
    		if (ss3<10) {
				ss2="0"+ss3;
			}
    		else{
    			ss2=""+ss3;
    		}
    		mytv_duration.setText(""+timezh.mm(max)+":"+ss2);
    	
    		
	        mTimeHandler = new Handler() {
	        	
	        	public void handleMessage(android.os.Message msg) { 
	        		
	        		if (msg.what == 0) {
	        			int dantime=mPlayer.getCurrentPosition();
	        			mypb_music_progress.setProgress(dantime);
	        			int ss=timezh.ss(dantime);
	        			String ss1=null;
	            		if (ss<10) {
	    					ss1="0"+ss;
	    				}
	            		else{
	            			ss1=""+ss;
	            		}
	            		
	            		mytv_currentposition.setText(""+""+timezh.mm(dantime)+":"+ss1);
	        			sendEmptyMessageDelayed(0, 1000); 
	        			} 
	        		}
	        	};   //在你的onCreate的类似的方法里面启动这个Handler就可以了： mTimeHandler.sendEmptyMessageDelayed(0, 1000);
	        	mTimeHandler.sendEmptyMessageDelayed(0, 1000);
	        	
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
						
					}
				});
			
		}


    }