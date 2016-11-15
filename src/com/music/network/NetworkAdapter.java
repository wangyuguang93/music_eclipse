package com.music.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.example.musicplay.R;
import com.music.network.BackAsyTask.Getmusic_ico;

/**
 * Created by Xiamin on 2016/8/28.
 */
public class NetworkAdapter extends BaseAdapter {
	private NetKGmusicInfo arr[];
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<NetKGmusicInfo> amusiclist;
    private Bitmap[] bitmap;
    private String[] net_pic_small;
   
    public NetworkAdapter(Context context,List<NetKGmusicInfo> musiclist,Bitmap[] bitmap)
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
    public View getView(int i, View view, ViewGroup viewGroup) {
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
    	
        ViewHolder viewHolder;
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.netmusic_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.img_network_item);
            viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.artist = (TextView) view.findViewById(R.id.tv_artist);
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
        return view;
    }

    class ViewHolder{
        ImageView image;
        TextView title;
        TextView artist;
    }
}