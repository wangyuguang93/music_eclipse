package com.music.network;

import android.R.string;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.musicplay.R;
import com.music.download.Net_music_download;
import com.music.network.BackAsyTask.Getmusic_ico;

/**
 * Created by Xiamin on 2016/8/28.
 */
public class NetworkAdapter extends BaseAdapter{
	private NetKGmusicInfo arr[];
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<NetKGmusicInfo> amusiclist;
    private Bitmap[] bitmap;
    private String[] net_pic_small;
    private String url;
    private String durl[];
   private int i;
    public NetworkAdapter(Context context,List<NetKGmusicInfo> musiclist)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        amusiclist=musiclist;
        this.bitmap=bitmap;
        
    }
    @Override
    public int getCount() {
       // return mList.size();
    	return amusiclist==null ? 0 : amusiclist.size(); 
    }

    @Override
    public Object getItem(int i) {
      //  return mList.get(i);
    	return amusiclist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
    	 arr = (NetKGmusicInfo[])amusiclist.toArray(new NetKGmusicInfo[amusiclist.size()]);
//    	 net_pic_small=new String[amusiclist.size()];
//    	 net_pic_small[i]=arr[i].getImgUrl();
//    	 Thread thread =new Thread(runnable);
//    	 if (i==amusiclist.size()-1) {
//    		 try {
//    				thread.start();
//    			} catch (Exception e) {
//    				// TODO: handle exception
//    				Log.d("不能下载图片", "不能下载图片");
//    				e.printStackTrace();
//    			}
//		}
    	this.i=i;
        ViewHolder viewHolder;
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.netmusic_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.img_network_item);
            viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.artist = (TextView) view.findViewById(R.id.tv_artist);
            viewHolder.iv_more=(ImageView) view.findViewById(R.id.iv_more);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
 //      viewHolder.image=arr[i].getFilename();
        String stile=arr[i].getFilename();
        if (stile!=null) {
        	viewHolder.title.setText(stile);
		}
        
        String sartist=arr[i].getSingername();
        if (stile!=null) {
        	viewHolder.artist.setText(sartist);
		}
       Bitmap pic=arr[i].getPic();
        if (pic!=null) {
			viewHolder.image.setImageBitmap(pic);
		}
        else {
			viewHolder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.deault_zhuanji));
		}
       viewHolder.iv_more.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.iv_more:
				//Toast.makeText(mContext, "点击了img", Toast.LENGTH_SHORT).show();
				
				 showPopupMenu(v,i);
				 
				break;

			default:
				break;
			}
		}
	});
      
        return view;
    }

    class ViewHolder{
        ImageView image;
        TextView title;
        TextView artist;
        ImageView iv_more;
    }

	 private void showPopupMenu(View view,final int index) {
		 // View当前PopupMenu显示的相对View的位置
		 PopupMenu popupMenu = new PopupMenu(mContext, view);
		 // menu布局
		 view.getId();
		
		 popupMenu.getMenuInflater().inflate(R.menu.option, popupMenu.getMenu());
		 // menu的item点击事件
		 popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
		  @Override
		  public boolean onMenuItemClick(MenuItem item) {
		 // Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_SHORT).show();
			  switch (item.getItemId()) {
			case R.id.option_download:
				List<String> list=new ArrayList<String>();
				List<String> downloadurl=new ArrayList<String>();
				String url128=arr[index].getUrl128();
				String url320=arr[index].getUrl320();
				String urlsq=arr[index].getUrlsq();
				String urlmv=arr[index].getUrlmv();

				if (url320!=null) {
					
					float size=Float.parseFloat(arr[index].getFilesize320());
					  DecimalFormat   fnum   =   new   DecimalFormat("##0.00");
					  size=++size/1024/1024;
					  String   dd=fnum.format(size); 
					list.add("高清音质"+"("+dd+"M)");
					downloadurl.add(url320);
				}
				if (url128!=null) {
					float size=Float.parseFloat(arr[index].getFilesize128());
					  DecimalFormat   fnum   =   new   DecimalFormat("##0.00");
					  size=++size/1024/1024;
					  String   dd=fnum.format(size); 
					list.add("标准音质"+"("+dd+"M)");
					downloadurl.add(url128);
				}
				if (urlsq!=null) {
					float size=Float.parseFloat(arr[index].getSqfilesize());
					  DecimalFormat   fnum   =   new   DecimalFormat("##0.00");
					  size=++size/1024/1024;
					  String   dd=fnum.format(size); 
					list.add("无损音质"+"("+dd+"M)");
					downloadurl.add(urlsq);
				}
				if (urlmv!=null) {
					float size=Float.parseFloat(arr[index].getSqfilesize());
					  DecimalFormat   fnum   =   new   DecimalFormat("##0.00");
					  size=++size/1024/1024;
					  String   dd=fnum.format(size); 
					list.add("MV"+"("+dd+"M)");
					downloadurl.add(urlmv);
				}
				final String dl[]=list.toArray(new String[list.size()]);
				durl=downloadurl.toArray(new String[list.size()]);
				AlertDialog.Builder download=new AlertDialog.Builder(mContext);
				download.setTitle("请选择");
				download.setSingleChoiceItems(dl, 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						int d=which;
						if (d==-1) {
							d=0;
						}
						url=durl[d];
					}
				});
				
				download.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {
							String songname=arr[index].getFilename();
							String extname=arr[index].getExtName();
							if (url==null) {
								url=durl[0];
							}
							System.out.print(arr[index].getFilesize320());
							Net_music_download net_music_download=new Net_music_download(mContext);
							net_music_download.execute(url,songname,extname);
							Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
					}
				});
				download.create().show();
				//Toast.makeText(mContext, arr[index].getSongname(), Toast.LENGTH_SHORT).show();
				
				break;
			case R.id.option_xiangqing:
				try {
					DecimalFormat   fnum   =   new   DecimalFormat("##0.00");
					 float size=Float.parseFloat(arr[index].getFilesize320());
					  size=++size/1024/1024;
					  String   dd=fnum.format(size); 
					AlertDialog.Builder netxiangqing=new AlertDialog.Builder(mContext);
					netxiangqing.setTitle("详情");
					netxiangqing.setMessage("文件名："+arr[index].getFilename()+"\n"+"专辑:"+arr[index].getAlbum_name()+"\n"+"大小:"+dd+"M");
					netxiangqing.create().show();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				 
				break;
			case R.id.option_play:
				Intent intent = new Intent();
				intent.putExtra("msg", "netplay");
				intent.putExtra("netindex",index);
				intent.setAction("main");
				mContext.sendBroadcast(intent);
				break;

			default:
				break;
			}
		  return false;
		  }
		 });
		 // PopupMenu关闭事件
		 popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
		  @Override
		  public void onDismiss(PopupMenu menu) {
		 // Toast.makeText(mContext, "关闭PopupMenu", Toast.LENGTH_SHORT).show();
		  }
		 });
		 popupMenu.show();
		 }
	
	
}