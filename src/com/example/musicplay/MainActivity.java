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
import android.media.MediaCryptoException;
import android.media.MediaDrmException;
import android.media.MediaDrmResetException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	private File dir;
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
	private TextView tv_duration,tv_currentposition,tv_music_title;
	private SeekBar pb_music_progress;
	private Handler mTimeHandler,myTimelenth;
	int max=0,danqian=0;
	private Intent intent;
	private String lujin;
	private Boolean islast;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		
		createmulu();
		Toast.makeText(this, dir.getPath().toString(), 1000).show();
		data=dir.list();
		listView=(ListView) findViewById(R.id.listview);
		ibPlayOrPuase=(ImageButton) findViewById(R.id.ib_play_pause);
		last=(ImageButton) findViewById(R.id.ib_previous);
		next=(ImageButton) findViewById(R.id.ib_next);
		tv_duration=(TextView) findViewById(R.id.tv_duration);
		tv_currentposition=(TextView) findViewById(R.id.tv_currentposition);
		pb_music_progress=(SeekBar) findViewById(R.id.pb_music_progress);
		tv_music_title=(TextView) findViewById(R.id.tv_music_title);
		

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
			if (istile.equals(test)) {
				islastwj=true;
				System.out.println("文件已找到");
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
		
		listView.setOnItemSelectedListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				musicIndex=position;
				myplay();
				
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
	}

	private void setListtem() {
		// TODO Auto-generated method stub
		last.setOnClickListener(this);
		next.setOnClickListener(this);
		ibPlayOrPuase.setOnClickListener(this);
		
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
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
		musicservice.play(dir, data, musicIndex, tv_duration, tv_currentposition, tv_music_title, pb_music_progress, MainActivity.this,islast,danqian);
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
	
	
}
