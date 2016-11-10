package com.music.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.VolumeProvider;
import android.provider.Settings.SettingNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.example.musicplay.R;

/**
 * Created by Xiamin on 2016/8/28.
 */
public class NetworkAdapter_item extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] net_song_name,net_author;
    private Bitmap[] bitmap;
    public NetworkAdapter_item(Context context,String[] net_song_name,String[] net_author,Bitmap[] bitmap)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.net_author=net_author;
        this.net_song_name=net_song_name;
        this.bitmap=bitmap;
       // mList = list;
    }


    @Override
    public int getCount() {
       // return mList.size();
    	return net_song_name.length;
    }

    @Override
    public Object getItem(int i) {
      //  return mList.get(i);
    	return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
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
        if (net_song_name!=null) {
        	viewHolder.title.setText(net_song_name[i]);
		}
       if (net_author!=null) {
    	   viewHolder.artist.setText(net_author[i]); 	   
	}
       if (bitmap[0]!=null) {
			viewHolder.image.setImageBitmap(bitmap[i]);
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