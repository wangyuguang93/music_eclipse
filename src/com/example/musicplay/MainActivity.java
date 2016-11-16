package com.example.musicplay;

import java.io.File;
import java.util.List;
import com.example.musicplay.BendiFragment.Funhui;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.music.network.BackAsyTask;
import com.music.network.BackAsyTask.Getmusic_ico;
import com.music.network.Errorfragment;
import com.music.network.KGmusicSearch;
import com.music.network.KGmusicSearch.KGSeachCallback;
import com.music.network.KGsearchResualt;
import com.music.network.MusicSearch;
import com.music.network.NetKGmusicInfo;
import com.music.network.MusicSearch.SeachCallback;
import com.music.network.Net_jiazai;
import com.music.network.NetworkAdapter;
import com.music.network.NetworkAdapter_item;
import com.music.network.Network_Musicinfo;
import com.music.network.Network_fragment;
import com.music.network.Network_fragment.Funhui_net;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(23)
public class MainActivity extends SlidingFragmentActivity implements OnClickListener,OnItemSelectedListener,Funhui,Funhui_net{
	private File dir;
	private String data[],bendi[],bendata[];
	private Bitmap[] testbendi;
	private int musicid[];
	private long music_albumId[];
	private int musicIndex=0;
	private ImageButton ibPlayOrPuase,last,next;
	private EditText edit_search;
	private ImageView img_ico,menu_ico,img_start,net_fanhui,net_search;
	private MyService musicservice;
	private boolean tag=false,pause=false,islastwj=false,iserji=false,is_updateListview=false;
	MusicAdpater musicAdpater;
	NetworkAdapter_item networkAdapter_item;
	static NetworkAdapter networkAdapter;
	private ListView listView,networklistview;
	private BroadcastReceiver main,erji,guanji;
	//int position;
	Boolean isplay=false;
	Boolean isdantime=false;
	int hg=0,huotime;
	private TextView tv_duration,tv_currentposition,tv_music_title,tv_guqu_num,tv_network,tv_bendi;
	private SeekBar pb_music_progress;
	int max=0,danqian=0;
	private Intent intent;
	private String lujin[];
	private String[] net_lujin,net_song_name,net_author,net_pic_small;
	private Boolean islast,isnetfinish=false,isfirst_net=true;
	private Cursor cursor;
	private Network_fragment netfragment;
	private BendiFragment bendiFrament;
	private Errorfragment errorfragment;
	private FragmentManager fragmentManager;
	private Net_jiazai net_jiazai;
	private FragmentTransaction fragmentTransaction;
	private Fragment mContent;
	private SousuoListFragment sousuo=new SousuoListFragment();
	private Bitmap[] bitmap,bendi_tupian;
	private RelativeLayout R_tille;
	private LinearLayout L_seach;
	private int num = 0; 
	private MusicSearch musicSearch;
	private KGmusicSearch kgmusicSearch;
	private String gequ="新歌";
	private String str;
	private NetKGmusicInfo arr[];
	private Boolean is_can_sousuo=true,isgequfinish=false;
	private static Thread updatethread;
	private static Runnable runnable;
	private int page=1;
	private Boolean isjiazai=true;
	int lastItem;
	private String istile;
    private Handler updateview = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 1) {  
            	if (networkAdapter!=null) {
            		networkAdapter.notifyDataSetChanged();
				}
            	
            }  
        }  
    };  
	private List<NetKGmusicInfo> musiclist;;
	@Override
	public void onCreate (Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		// this.requestPermissions(permissions, requestCode);
		//File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		//创建广播接收器
		main=new mainReceiver();
        IntentFilter filter = new IntentFilter("main");  
        registerReceiver(main, filter);  
        System.out.println("main广播接收器服务被创建...."); 

        
        //关机广播
        guanji=new mainReceiver();
        IntentFilter myguanji=new IntentFilter("android.intent.action.ACTION_SHUTDOWN");
        registerReceiver(guanji, myguanji);
        //////////////
		createmulu();
		ScannerMusic();
		//Toast.makeText(this, dir.getPath().toString(), 1000).show();
		
		findId();
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<musicid.length;i++){
				testbendi[i] =MediaUtil.getArtwork(MainActivity.this,musicid[i], music_albumId[i], true, true);
				}
			}
		});
		thread.start();
		qiehuanListview(0);
  //读取数据
		try {
			SharedPreferences peizhi=this.getSharedPreferences("peizhi", Context.MODE_PRIVATE);
			istile=peizhi.getString("tile", null);
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
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//Log.d("wenti", ""+peizhi.getInt("danqian", 0));
		Connection();		
//nnn		musicAdpater=new MusicAdpater(this, data,listView,musicIndex);
///nnn		listView.setAdapter(musicAdpater);
		//listView.setSelection(0);
		
		setListtem();
		//musicservice.mPlerFinish();
		//
		
		//检查文件
		int i=0;
		for(i=0;i<bendata.length-1;i++)
		{
			String test=bendata[i].toString();
			if (istile==null) {
				Log.d("aa", istile+"---"+test);
				break;
			}
			if (istile.equals(test)) {
				Log.d("bb", istile+"---"+test);
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
//nn		listView.setSelection(musicIndex);
		//再次取得danqian的值
		danqian=pb_music_progress.getProgress();
		//设置左滑菜单
		initSlidingMenu(savedInstanceState);
////nnn	listView.setSelection(musicIndex);
//		listView.setOnItemSelectedListener(this);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				musicIndex=position;
//				islast=false;
//				pb_music_progress.setProgress(0);
//				myplay();
//				
//				}
//			
//			
//		});
		
		
		
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
	bendata=new String[cursor.getCount()];
	bendi=new String[cursor.getCount()];
	musicid=new int[cursor.getCount()];
	music_albumId=new long[cursor.getCount()];
	testbendi =new Bitmap[cursor.getCount()];
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
	long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
	// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
	Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
	// 歌曲文件显示名字
	String disName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
	
	//Log.e("music disName=", ""+cursor.getCount());//打印出歌曲名字
	bendata[j]=disName;
	bendi[j]=url;
	musicid[j]=id;
	music_albumId[j]=albumId;
	
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
		menu_ico.setOnClickListener(this);
		tv_network.setOnClickListener(this);
		tv_bendi.setOnClickListener(this);
		img_start.setOnClickListener(this);
		net_fanhui.setOnClickListener(this);
		net_search.setOnClickListener(this);
		tv_guqu_num.setOnClickListener(this);
		
		 
		//监听软键盘的删除键  
		edit_search.setOnKeyListener(new View.OnKeyListener() {  
		            @Override  
		            public boolean onKey(View v, int keyCode, KeyEvent event) {  
		                if (keyCode == KeyEvent.KEYCODE_DEL) {  
		                    num++;  
		                    //在这里加判断的原因是点击一次软键盘的删除键,会触发两次回调事件  
		                    if (num % 2 != 0) {  
		                        String s = edit_search.getText().toString();  
		                        if (!TextUtils.isEmpty(s)) {  
		                        	edit_search.setText("" + s.substring(0, s.length() - 1));  
		                            //将光标移到最后  
		                            edit_search.setSelection(edit_search.getText().length());  
		                        }  
		                    }  
		                    return true;  
		                }  
		                return false;  
		            }  
		        });      
	}
	//找控件
	private void findId(){
		
		ibPlayOrPuase=(ImageButton) findViewById(R.id.ib_play_pause);
		last=(ImageButton) findViewById(R.id.ib_previous);
		next=(ImageButton) findViewById(R.id.ib_next);
		tv_duration=(TextView) findViewById(R.id.tv_duration);
		tv_currentposition=(TextView) findViewById(R.id.tv_currentposition);
		pb_music_progress=(SeekBar) findViewById(R.id.pb_music_progress);
		tv_music_title=(TextView) findViewById(R.id.tv_music_title);
		tv_guqu_num=(TextView) findViewById(R.id.tv_gequ_num);
		img_ico=(ImageView) findViewById(R.id.img_ico);
		menu_ico=(ImageView) findViewById(R.id.menu_ico);
		tv_network=(TextView) findViewById(R.id.tv_network);
		tv_bendi=(TextView) findViewById(R.id.tv_bendi);
		img_start=(ImageView) findViewById(R.id.imageView2);
		net_fanhui=(ImageView) findViewById(R.id.net_fanhui);
		net_search=(ImageView) findViewById(R.id.net_search);
		R_tille=(RelativeLayout) findViewById(R.id.r_tille);
		L_seach=(LinearLayout) findViewById(R.id.search_view);
		edit_search=(EditText) findViewById(R.id.edit_search);
		
		
		
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
	public void onClick(View v) {
		// TODO Auto-generated method stub		
		switch (v.getId()) {
		case R.id.ib_play_pause:
			lujin=bendi;
			data=bendata;
			bendi_tupian=testbendi;
			bofang();
			intent  = new Intent(); 
	        intent.putExtra("msg", "update_tongzhi");
	        intent.setAction("guang93");  
	        sendBroadcast(intent);  
			break;
		case R.id.ib_previous:
			lujin=bendi;
			data=bendata;			
			bendi_tupian=testbendi;
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
			lujin=bendi;
			data=bendata;
			
			bendi_tupian=testbendi;
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
		case R.id.menu_ico:
			toggle();
			break;
		case R.id.tv_gequ_num:
			page++;
			if (!gequ.equals("")) {
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																															
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																															//删除歌词缓存目录
				File lcylujiu=Environment.getExternalStorageDirectory();
				File lcydir=new File(lcylujiu.toString()+"/kgmusic/"+"/lcy/"+"/Cache/");
				delete(lcydir);	
				if (is_can_sousuo) {
					is_can_sousuo=false;
					isnetfinish=false;
//					netfragment=null;
					networkAdapter_item=null;
					yincang();
					net_jiazai=null;				
					qiehuanListview(1);
					netfragment=null;
					//networkplay(gequ);
					Runnable runnable1=new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Kgmusic(gequ,page);
						}
					};
					Handler handler1=new Handler();
					try {
						handler1.post(runnable1);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					hg=-1;
					Runnable runnable2=new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							try {
								Thread.sleep(60000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
					    	
					    	//String str1=arr[arr.length-1].getFilename();
							
					    	if (!is_can_sousuo) {
					    		try {
					    			qiehuanListview(2);
								} catch (Exception e) {
									// TODO: handle exception
									is_can_sousuo=true;
									arr=null;
									e.printStackTrace();
								}
								
								is_can_sousuo=true;
								arr=null;
								
							}
					    	
						}
					};
					Thread thread2=new Thread(runnable2);
					try {
						thread2.start();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}else {
					Toast.makeText(MainActivity.this, "正在搜索", Toast.LENGTH_SHORT).show();
				}

			
		}
			break;
		case R.id.tv_network:
			is_can_sousuo=false;
			if (isfirst_net) {
				Kgmusic("新歌",1);
				isfirst_net=false;				
				
			}
			Runnable runnable3=new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(40000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
			    	
			    	//String str1=arr[arr.length-1].getFilename();
			    	if (!is_can_sousuo) {
			    		try {
			    			qiehuanListview(2);
							is_can_sousuo=true;
							arr=null;
						} catch (Exception e) {
							// TODO: handle exception
							is_can_sousuo=true;
							arr=null;
							e.printStackTrace();
						}
						
						
					}
			    	
				}
			};
			Thread thread3=new Thread(runnable3);
			
			try {
				thread3.start();
			} catch (Exception e) {
				// TODO: handle exception
				is_can_sousuo=true;
				arr=null;
				e.printStackTrace();
			}
			
			qiehuanListview(1);
			
			//Log.d("test", "切换到网络视图");
			break;
		case R.id.tv_bendi:
		qiehuanListview(0);
		//Log.d("test", "切换到本地视图");
			break;
		case R.id.imageView2:
			R_tille.setVisibility(View.GONE);
			L_seach.setVisibility(View.VISIBLE);
			
//			KGmusicSearch kGmusicSearch=new KGmusicSearch();
//			kGmusicSearch.search("恋人心");
			
			break;
		case R.id.net_fanhui:
			L_seach.setVisibility(View.GONE);
			R_tille.setVisibility(View.VISIBLE);
			break;
		case R.id.net_search:
			gequ=edit_search.getText().toString();	
			KGsearchResualt.setinfo();
			if (!gequ.equals("")) {
				
				//删除歌词缓存目录
				File lcylujiu=Environment.getExternalStorageDirectory();
				File lcydir=new File(lcylujiu.toString()+"/kgmusic/"+"/lcy/"+"/Cache/");
				delete(lcydir);	
				if (is_can_sousuo) {
					is_can_sousuo=false;
					isnetfinish=false;
//					netfragment=null;
					networkAdapter_item=null;
					networkAdapter=null;
					
					yincang();
					net_jiazai=null;				
					qiehuanListview(1);
					netfragment=null;
					//networkplay(gequ);
					Runnable runnable1=new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							isjiazai=false;
							page=1;
							Kgmusic(gequ,page);
						}
					};
					Handler handler1=new Handler();
					try {
						handler1.post(runnable1);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					hg=-1;
					Runnable runnable2=new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							try {
								Thread.sleep(40000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
					    	
					    	//String str1=arr[arr.length-1].getFilename();
							
					    	if (!is_can_sousuo) {
					    		try {
					    			qiehuanListview(2);
								} catch (Exception e) {
									// TODO: handle exception
									is_can_sousuo=true;
									arr=null;
									e.printStackTrace();
								}
								
								is_can_sousuo=true;
								arr=null;
								
							}
					    	
						}
					};
					Thread thread2=new Thread(runnable2);
					try {
						thread2.start();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}else {
					Toast.makeText(MainActivity.this, "正在搜索", Toast.LENGTH_SHORT).show();
				}

			}
			break;
	
		default:
			break;
		}
		//fragmentTransaction.commit();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		this.musicIndex=position;
		//isplay=false;
	}
	
	/**
	 * 隐藏所以得Frament
	 */
		
	public void yincang() {
		if (bendiFrament!=null) {
			//bendiFrament=(BendiFragment) fragmentManager.findFragmentByTag("bendiFrament");
			fragmentTransaction.hide(bendiFrament);	
			Log.d("test", "隐藏bendiFrament");
		}
		if (netfragment!=null) {
			//netfragment=(Network_fragment) fragmentManager.findFragmentByTag("netfragment");
			fragmentTransaction.hide(netfragment);
			Log.d("test", "隐藏netfragment");
		}
		if (net_jiazai!=null) {
			fragmentTransaction.hide(net_jiazai);
		}
		if (errorfragment!=null) {
			fragmentTransaction.hide(errorfragment);
		}
	}

	public void myplay() {
		
        //耳机插拔广播
        //IntentFilter myerji=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
		if (tag==false) {
			erji=new erjiReceiver();	       
	        IntentFilter intentFilter=new IntentFilter(); 
	        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
	        registerReceiver(erji, intentFilter);   
		}
	
		if (isplay&&hg==musicIndex) {
			musicservice.jixuplay();
			pause=true;
			ibPlayOrPuase.setImageResource(android.R.drawable.ic_media_pause);
			
		}
		else{
		musicservice.play(data,lujin,
				musicIndex, tv_duration, 
				tv_currentposition, 
				tv_music_title, 
				pb_music_progress, 
				MainActivity.this,
				islast,danqian,
				tv_guqu_num,
				musicid,
				music_albumId,img_ico,
				bendi_tupian);
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
		if (main!=null) {
			unregisterReceiver(main);
		}
		if (erji!=null) {
			unregisterReceiver(erji);
		}
		if (guanji!=null) {
			unregisterReceiver(guanji);
		}
		super.onDestroy();
	}
	
	@Override  
	 public boolean onKeyDown(int keyCode, KeyEvent event) { 
			if (!tag) {
				finish();
				return true;
			}
			else{
	        if (keyCode == KeyEvent.KEYCODE_BACK&&tag) {  
	            moveTaskToBack(false);  
	            return true;  
	        }  
			}
	        return super.onKeyDown(keyCode, event);  
	    }  
	
	private class mainReceiver extends BroadcastReceiver{  
		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            // TODO Auto-generated method stub 
        	String msg=intent.getStringExtra("msg");
        	String action=intent.getAction();
        	if (msg.equals("play")) {
        		Log.d("test", msg);
        	bofang();
        	Intent play  = new Intent(); 
        	play.putExtra("msg", "update_tongzhi");
	        play.setAction("guang93");  
	        sendBroadcast(play);  
        	
        	}
        	if (msg.equals("exit")) {
        	Log.d("test", "退出");
        	finish();
        	
        	}
        	
        	if (msg.equals("android.intent.action.ACTION_SHUTDOWN")) {
				finish();
			}

        	if (msg.equals("listview")) {
//        		int zhi=intent.getIntExtra("zhi", musicIndex);
//        		musicAdpater.update(musicIndex, zhi);
//        		musicIndex=zhi;
//        		//Log.d("gggg", ""+zhi);
//        		 Handler handler=new Handler();
//        		 Runnable add=new Runnable(){
//        			 @Override
//        			 public void run() {
//        			  // TODO Auto-generated method stub
//        			 //arr.add("增加一项");//增加一项
//        			  musicAdpater.notifyDataSetChanged();    
//        			}  
//        		 };
//        	
//        		 handler.post(add);
//        		musicAdpater.notifyDataSetChanged();
//        		//listView.setAdapter(musicAdpater);
           	}
        }
    }  	
	private class erjiReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			String action=intent.getAction();
//			if (action.equals("android.intent.action.HEADSET_PLUG")) {  
//				Log.d("guangbo", "ok");
//				Intent erplay = new Intent();
//				erplay.putExtra("msg", "play");
//				erplay.setAction("main");
//				sendBroadcast(erplay);
//            }  
			if (iserji==false) {
				iserji=true;
				
			}else {
				 if(intent.hasExtra("state")){  
		                if(intent.getIntExtra("state", 0)==0){  
		                		if (musicservice.isPlay()) {
		                			bofang();
						        	Intent play  = new Intent(); 
						        	play.putExtra("msg", "update_tongzhi");
							        play.setAction("guang93");  
							        sendBroadcast(play); 
								}
								 
		 
		                }  
		                else if(intent.getIntExtra("state", 0)==1){  
		                  //  Toast.makeText(context, "headset  connected", Toast.LENGTH_LONG).show();
		                	
							bofang();
				        	Intent play  = new Intent(); 
				        	play.putExtra("msg", "update_tongzhi");
					        play.setAction("guang93");  
					        sendBroadcast(play);  
		                }  
		                
		            }  
			}
           
		}
		
	}
	

	//播放
public void bofang() {
	if (isplay&&pause) {
		musicservice.pause();
		ibPlayOrPuase.setImageResource(android.R.drawable.ic_media_play);
		pause=false;
		networkAdapter.notifyDataSetChanged();
	}			
	else {
		myplay();
		networkAdapter.notifyDataSetChanged();
	}
	
}
/**
 * 切换Fragment
 * 
 * @param fragment
 */
public void switchConent(Fragment fragment) {
	mContent = fragment;
	getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, fragment).commit();
	getSlidingMenu().showContent();
}

LeftFragment leftFragment=new LeftFragment(MainActivity.this);
private void initSlidingMenu(Bundle savedInstanceState) {
	// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
	if (savedInstanceState != null) {
		mContent = getSupportFragmentManager().getFragment(
				savedInstanceState, "mContent");
	}

	if (mContent == null) {
		mContent = new TodayFragment();
	}

	// 设置左侧滑动菜单
	setBehindContentView(R.layout.menu_frame_left);
	getSupportFragmentManager().beginTransaction()
			.replace(R.id.menu_frame, leftFragment).commit();

	// 实例化滑动菜单对象
	SlidingMenu sm = getSlidingMenu();
	// 设置可以左右滑动的菜单
	sm.setMode(SlidingMenu.LEFT);
	// 设置滑动阴影的宽度
	sm.setShadowWidthRes(R.dimen.shadow_width);
	// 设置滑动菜单阴影的图像资源
	sm.setShadowDrawable(null);
	// 设置滑动菜单视图的宽度
	//sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	// 设置渐入渐出效果的值
	sm.setFadeDegree(0.35f);
	// 设置触摸屏幕的模式,这里设置为全屏
	sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	// 设置下方视图的在滚动时的缩放比例
	sm.setBehindScrollScale(0.0f);

}

@Override
public void fanhuiView(View view) {
	// TODO Auto-generated method stub
	listView=(ListView) view.findViewById(R.id.bendi_listView);
	if (!is_updateListview) {
		musicAdpater=new MusicAdpater(this, bendata,listView,musicIndex,musicid,music_albumId);
		listView.setAdapter(musicAdpater);
		//listView.setSelection(musicIndex);
		listView.setOnItemSelectedListener(this);
		is_updateListview=true;
		is_can_sousuo=true;
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				lujin=bendi;
				data=bendata;
				musicIndex=position;
				bendi_tupian=testbendi;
				islast=false;
				pb_music_progress.setProgress(0);
				myplay();
				
				}
			
			
		});
	}
}
	
/**
 * 切换listviewdeFragment
 */
public void qiehuanListview(int index) {
	// TODO Auto-generated method stub
	
	fragmentManager=getFragmentManager();
	fragmentTransaction=fragmentManager.beginTransaction();
	yincang();
	switch (index) {
	case 0:
		if (bendiFrament==null) {
			bendiFrament=new BendiFragment(MainActivity.this);
			fragmentTransaction.add(R.id.l_contor,bendiFrament,"bendiFrament");
			tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
		}else {
			fragmentTransaction.show(bendiFrament);
			tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
			tv_network.setTextColor(MainActivity.this.getResources().getColor(R.color.heise));
		
		}
		break;
	case 1:
		if (!isnetfinish) {
			if (net_jiazai==null) {
				net_jiazai=new Net_jiazai();
				fragmentTransaction.add(R.id.l_contor, net_jiazai,"net_jiazai");
				tv_network.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
				tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.heise));
				
			}
			else {
				fragmentTransaction.show(net_jiazai);
				tv_network.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
				tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.heise));
				
			}
			//Log.d("加载中", "加载中");
		}
			
		else {
			
		if (net_jiazai!=null) {
			fragmentTransaction.remove(net_jiazai);
		}
		
		if (netfragment==null) {
			netfragment=new Network_fragment(MainActivity.this);
			fragmentTransaction.add(R.id.l_contor,netfragment,"netfragment");
			tv_network.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
			tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.heise));
			
		}else {
			fragmentTransaction.show(netfragment);
			tv_network.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
			tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.heise));
			
		}
		
		}
		break;
		case 2:
		if (errorfragment==null) {
			errorfragment=new Errorfragment(MainActivity.this);
			fragmentTransaction.add(R.id.l_contor,errorfragment,"errorfragment");
		//	tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
		}else {
			fragmentTransaction.show(errorfragment);
//			tv_bendi.setTextColor(MainActivity.this.getResources().getColor(R.color.white));
//			tv_network.setTextColor(MainActivity.this.getResources().getColor(R.color.heise));
		
		}
		break;
	default:
		break;
	}
	fragmentTransaction.commit();
}
/**
 * net回调
 */
@Override
public void fanhuiView_net(View view) {
	// TODO Auto-generated method stub
	Log.d("aa", "1");
	
	networklistview=(ListView) view.findViewById(R.id.network_listView);
//	
//	if (networkAdapter_item==null) {
//		networkAdapter_item=new NetworkAdapter_item(MainActivity.this,net_song_name,net_author,bitmap);	
//		
//		}
/**
 * 下拉测试	
 */
	
	
	if (networkAdapter==null) {
		networkAdapter=new NetworkAdapter(MainActivity.this, musiclist,bitmap);
	}
		networklistview.setAdapter(networkAdapter);
		//networklistview.setAdapter(networkAdapter_item);
		is_can_sousuo=true;
	//设置监听器
	//下拉刷新
		networklistview.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (lastItem == networklistview.getCount()-1  
		                && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
		  
		          Log.d("isjiazai", ""+isjiazai);
		          if (isjiazai) {
		        	  page++;
				}
		            
		            if (page<=5&&isjiazai) {
		            	KGsearchResualt.setupdatelistview();
		            	is_can_sousuo=false;
		            	isjiazai=false;
		            	  kgmusicSearch.search(gequ,page);
				            kgmusicSearch.setKGCallBack(new KGSeachCallback() {
								
								@Override
								public void onSearchResult(List<NetKGmusicInfo> resualt) {
									// TODO Auto-generated method stub
									  Log.e("加载更多", "加载更多歌曲");
								//	bitmap=null;
									
									setnetlistview(resualt);
									
								}
							});
					}
		          
			}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				 lastItem = firstVisibleItem + visibleItemCount - 1;
			      //  Log.i("屏幕滚动","firstVisibleItem:"+firstVisibleItem+"visibleItemCount:"+visibleItemCount+" lastItem:"+lastItem+"networklistview.getCount:"+networklistview.getCount());  
			}
		});
		//点击事件
		networklistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				//networkAdapter.getView(position, view, networklistview);
				
				if (net_lujin[position]==null) {
					updatethread=new Thread(runnable);
					updatethread.start();
				}
				else {
					
					musicIndex=position;
					lujin=net_lujin;
					data=net_song_name;
					bendi_tupian=bitmap;
					islast=false;
					pb_music_progress.setProgress(0);
					Handler playhandler=new Handler();
					Runnable playrunnable=new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							myplay();
						}
					};
					playhandler.post(playrunnable);
				}
			}
		});
		
}

//获取网络歌曲
public void networkplay(String guqu) {
	//判断有无网络
	//Log.d("aa", "2");
	if (musicSearch==null) {
		musicSearch=new MusicSearch();
	}
	
	
	musicSearch.search(guqu);
	musicSearch.setCallBack(new SeachCallback() {
		
		@Override
		public void onSearchResult(List<Network_Musicinfo> resualt) {
			// TODO Auto-generated method stub
			
			int i=resualt.size();
			
			Log.d("i", ""+i);
			int size = resualt.size(); 
			net_lujin=new String[size];
			net_author=new String[size];
			net_song_name=new String[size];
			net_pic_small=new String[size];
			bitmap=new Bitmap[size];
			
			Network_Musicinfo arr[] = (Network_Musicinfo[])resualt.toArray(new Network_Musicinfo[size]);//使用了第二种接口，返回值和参数均为结果  
			
			for(int j=0;j<size;j++){
				//System.out.println(arr[i].getmMusicPath().toString());
				net_lujin[j]=arr[j].getmMusicPath();
				net_author[j]=arr[j].getMauthor();
				net_song_name[j]=arr[j].getMtitle();
				net_pic_small[j]=arr[j].getMpic_small();
				//System.out.println(net_lujin[j].toString());
				//update_listview();
				
			}
			
			BackAsyTask backAsyTask=new BackAsyTask(net_pic_small);
				
			backAsyTask.execute("pic");
			backAsyTask.setGetmusic(new Getmusic_ico() {
				
				@Override
				public void getmusic_ico(Bitmap[] resualt) {
//					 TODO Auto-generated method stub
					
					for (int j = 0; j < resualt.length; j++) {
						bitmap[j]=resualt[j];
						networkAdapter_item.notifyDataSetChanged();
					}
				//bitmap=resualt;	
				
				Log.d("图片", "图片下载完成");
				
				}
			});
			isnetfinish=true;
			qiehuanListview(1);
		}
	});
	
}
/**
 * 获取KG
 */
public void Kgmusic(String gequ,int index) {
	// TODO Auto-generated method stub
	this.gequ=gequ;
	try {
		if (kgmusicSearch==null) {
			kgmusicSearch=new KGmusicSearch();
		}	
		kgmusicSearch.search(this.gequ,page);
		kgmusicSearch.setKGCallBack(new KGSeachCallback() {
			
			@Override
			public void onSearchResult(List<NetKGmusicInfo> resualt) {
				
				setnetlistview(resualt);
				
			}
			
			
		});


		
	} catch (Exception e) {
		// TODO: handle exception
		is_can_sousuo=true;
		arr=null;
		e.printStackTrace();
	}
	
}
public static void Update(){
	updatethread=new Thread(runnable);
	updatethread.start();
}

/**
 * 递归删除文件及文件夹  
 * @param file
 */
public static void delete(File file) {  
    if (file.isFile()) {  
        file.delete();  
        return;  
    }  

    if(file.isDirectory()){  
        File[] childFiles = file.listFiles();  
        if (childFiles == null || childFiles.length == 0) {  
            file.delete();  
            return;  
        }  
  
        for (int i = 0; i < childFiles.length; i++) {  
            delete(childFiles[i]);  
        }  
        file.delete();  
    }  
}  
public void setnetlistview(List<NetKGmusicInfo> resualt) {

	// TODO Auto-generated method stub
	is_can_sousuo=false;
	musiclist=resualt;
	int i=resualt.size();
	
	Log.d("i", ""+i);
	int size = resualt.size(); 
	net_lujin=new String[size];
	net_author=new String[size];
	net_song_name=new String[size];
	net_pic_small=new String[size];
	str=new String();
	bitmap=new Bitmap[size];
	arr = (NetKGmusicInfo[])resualt.toArray(new NetKGmusicInfo[size]);//使用了第二种接口，返回值和参数均为结果  
	for(int j=0;j<size;j++){
		//System.out.println(arr[i].getmMusicPath().toString());
		
		net_author[j]=arr[j].getSingername();
		//net_song_name[j]=arr[j].getSongname();
		net_song_name[j]=arr[j].getFilename();
		
		

	}
	
	runnable=new Runnable() {  
	    @Override  
	    public void run() {  
	        // TODO Auto-generated method stub  
	        //要做的事情  
	    	try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	    	
	    	str=arr[arr.length-1].getUrl128();
			if (str!=null) {
				for(int a=0;a<arr.length;a++){
				net_lujin[a]=arr[a].getUrl320();
				if(net_lujin[a]==null){
					net_lujin[a]=arr[a].getUrl128();														
				}
				try {
					String pic=arr[a].getImgUrl();
					net_pic_small[a]=pic.replace("{size}", "200");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
				}
				BackAsyTask backAsyTask=new BackAsyTask(net_pic_small);				
				backAsyTask.execute("pic");
				backAsyTask.setGetmusic(new Getmusic_ico() {
					
					@Override
					public void getmusic_ico(Bitmap[] resualt) {
//						 TODO Auto-generated method stub
						
						for (int j = 0; j < resualt.length; j++) {
							bitmap[j]=resualt[j];
							arr[j].setPic(resualt[j]);
//							try {
//								//networkAdapter_item.notifyDataSetChanged();
//								networkAdapter.notifyDataSetChanged();
//							} catch (Exception e) {
//								// TODO: handle exception
//								e.printStackTrace();
//							}
							
							Message msg = new Message();  
				            msg.what = 1;  
				            updateview.sendMessage(msg);  
							
						}
					Log.d("图片", "图片下载完成");
					isjiazai=true;
					}
				});
			}
	      
	    }  
	};  

	updatethread=new Thread(runnable);
	isnetfinish=true;
	is_can_sousuo=true;
	qiehuanListview(1);
//	try {
//		updatethread.start();
//	} catch (Exception e) {
//		// TODO: handle exception
//		e.printStackTrace();
//	}
	
}

}
