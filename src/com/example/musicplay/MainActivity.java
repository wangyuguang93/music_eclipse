package com.example.musicplay;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaCryptoException;
import android.media.MediaDrmException;
import android.media.MediaDrmResetException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	private File dir,test;
	private MediaPlayer player;
	private String data[];
	private int musicIndex=0;
	private ImageButton ibPlayOrPuase,last,next;
	private static String TAG="MusicService";
	private MyService musicservice;
	private boolean tag=false,pause=false,islastwj=false;
	MusicAdpater musicAdpater;
	ListView listView;
	//int position;
	Boolean isplay=false;
	Boolean isdantime=false;
	int hg=0,huotime;
	private TextView tv_duration,tv_currentposition,tv_music_title,tv_guqu_num;
	private SeekBar pb_music_progress;
	private Handler mTimeHandler,myTimelenth;
	int max=0,danqian=0;
	private Intent intent;
	private String lujin[];
	private Boolean islast;
	private Cursor cursor;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		
		createmulu();
		ScannerMusic();
		Toast.makeText(this, dir.getPath().toString(), 1000).show();
		
		findId();

  //读取数据
		SharedPreferences peizhi=this.getSharedPreferences("peizhi", Context.MODE_PRIVATE);
		String istile=peizhi.getString("tile", null);
		tv_music_title.setText(istile);
		max=peizhi.getInt("max", 0);
		pb_music_progress.setMax(max);
		pb_music_progress.setProgress(peizhi.getInt("dantime", 0));
		tv_duration.setText(peizhi.getString("length", null));
		tv_currentposition.setText(peizhi.getString("danqian_length", null));
		musicIndex=peizhi.getInt("musicIndex", 0);
		islast=peizhi.getBoolean("ismylast", false);
		danqian=peizhi.getInt("dantime", 0);
		tv_guqu_num.setText(peizhi.getString("gequ_num", ""));
		//Log.d("wenti", ""+peizhi.getInt("danqian", 0));
		
		Connection();		
		musicAdpater=new MusicAdpater(this, data,listView);
		listView.setAdapter(musicAdpater);
		//listView.setSelection(0);
		
		setListtem();
		//musicservice.mPlerFinish();
		//
		
		//检查文件
		int i=0;
		for(i=0;i<data.length-1;i++)
		{
			String test="播放："+data[i].toString();
			if (istile==null) {
				break;
			}
			if (istile.equals(test)) {
				islastwj=true;
				System.out.println("文件已找到");
				musicIndex=i;
				break;
			}
			System.out.println(test);

		}
		if (!islastwj) {
			Log.d("是否存在文件", "没有找到"+istile);
			pb_music_progress.setMax(0);
			pb_music_progress.setProgress(0);
			tv_duration.setText("");
			tv_currentposition.setText("");
			tv_music_title.setText("");
			islast=false;
		}
		//再次取得danqian的值
		danqian=pb_music_progress.getProgress();
		listView.setSelection(musicIndex);
		listView.setOnItemSelectedListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				musicIndex=position;
				islast=false;
				pb_music_progress.setProgress(0);
				myplay();
				
				}
			
			
		});
		
		pb_music_progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				danqian=seekBar.getProgress();
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

	private void createmulu() {
		// TODO Auto-generated method stub
		File lujiu=Environment.getExternalStorageDirectory();
		dir=new File(lujiu+"/kgmusic/"+"/download/");
		if (!dir.exists()) {
			dir.mkdirs();
			
		}
	//data=dir.list();
	}

	
	public void ScannerMusic()
	{
	// 查询媒体数据库
	cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
	MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	// 遍历媒体数据库
	Log.d("music disName=", ""+cursor.getCount());//打印出歌曲名字
	data=new String[cursor.getCount()];
	lujin=new String[cursor.getCount()];
	int j=0;
	if (cursor.moveToFirst())
	{
	while (!cursor.isAfterLast())
	{
		
	// 歌曲编号
	int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
	// 歌曲id
	int trackId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
	// 歌曲标题
	String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
	// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
	String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
	// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
	String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
	// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
	String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
	// 歌曲的总播放时长：MediaStore.Audio.Media.DURATION
	int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
	// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
	Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
	// 歌曲文件显示名字
	String disName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
	//Log.e("music disName=", ""+cursor.getCount());//打印出歌曲名字
	data[j]=disName;
	lujin[j]=url;
	//Log.e("music disName=", ""+test1[j]);//打印出歌曲名字
	cursor.moveToNext();
	j++;
	}
	cursor.close();
	}
	}
	
	private void setListtem() {
		// TODO Auto-generated method stub
		last.setOnClickListener(this);
		next.setOnClickListener(this);
		ibPlayOrPuase.setOnClickListener(this);
		
	}
	//找控件
	private void findId(){
		listView=(ListView) findViewById(R.id.listview);
		ibPlayOrPuase=(ImageButton) findViewById(R.id.ib_play_pause);
		last=(ImageButton) findViewById(R.id.ib_previous);
		next=(ImageButton) findViewById(R.id.ib_next);
		tv_duration=(TextView) findViewById(R.id.tv_duration);
		tv_currentposition=(TextView) findViewById(R.id.tv_currentposition);
		pb_music_progress=(SeekBar) findViewById(R.id.pb_music_progress);
		tv_music_title=(TextView) findViewById(R.id.tv_music_title);
		tv_guqu_num=(TextView) findViewById(R.id.tv_gequ_num);
		
	}
	private void Connection(){
		Intent intent=new Intent(MainActivity.this, MyService.class);
		bindService(intent, sc, Context.BIND_AUTO_CREATE);
	}
	private ServiceConnection sc=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			musicservice=null;
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			musicservice=((MyService.MusicBinder)(service)).getService();
			if (musicservice!=null) {
				//musicservice.play(dir, data, musicIndex, ibPlayOrPuase);
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_exit:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_play_pause:
			if (isplay&&pause) {
				musicservice.pause();
				ibPlayOrPuase.setImageResource(android.R.drawable.ic_media_play);
				pause=false;
				
			}			
			else {
				myplay();
			}
			break;
		case R.id.ib_previous:
			if (!tag) {
				musicIndex--;
				islast=false;
				pb_music_progress.setProgress(0);
				myplay();
			}
			else {
		        intent  = new Intent(); 
		        intent.putExtra("msg", "last");
		        intent.setAction("guang93");  
		        sendBroadcast(intent);  
			}

			break;

		case R.id.ib_next:
			//next();
			if (!tag) {
				musicIndex++;
				islast=false;
				pb_music_progress.setProgress(0);
				myplay();
			}
			else {
		        intent  = new Intent(); 
		        intent.putExtra("msg", "next");
		        intent.setAction("guang93");  
		        sendBroadcast(intent);  
				
			}

			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		this.musicIndex=position;
		//isplay=false;
	}
	public void myplay() {
	
		if (isplay&&hg==musicIndex) {
			musicservice.jixuplay();
			pause=true;
			ibPlayOrPuase.setImageResource(android.R.drawable.ic_media_pause);
			
		}
		else{
		musicservice.play(lujin, musicIndex, tv_duration, tv_currentposition, tv_music_title, pb_music_progress, MainActivity.this,islast,danqian,tv_guqu_num);
		//Toast.makeText(MainActivity.this, "播放中", 1000).show();
		tag=true;
		isplay=true;
		pause=true;
		islast=false;
		//listView.setSelection(musicIndex);
		ibPlayOrPuase.setImageResource(android.R.drawable.ic_media_pause);
		hg=musicIndex;
		//Toast.makeText(MainActivity.this, ""+musicIndex, 1000).show();
		}

	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		//this.musicIndex=0;
		isplay=false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindService(sc);
		super.onDestroy();
	}
	
	@Override  
	 public boolean onKeyDown(int keyCode, KeyEvent event) {  
	        if (keyCode == KeyEvent.KEYCODE_BACK) {  
	            moveTaskToBack(false);  
	            return true;  
	        }  
	        return super.onKeyDown(keyCode, event);  
	    }  

}
