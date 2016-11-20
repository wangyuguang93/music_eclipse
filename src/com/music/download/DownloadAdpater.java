package com.music.download;

import com.example.musicplay.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadAdpater extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    
    public DownloadAdpater(Context context) {
		// TODO Auto-generated constructor stub
    	 mContext = context;
         mLayoutInflater = LayoutInflater.from(mContext);
	}
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ViewHolder viewHolder;
	        if(convertView == null)
	        {
	        	convertView = mLayoutInflater.inflate(R.layout.download_item, null);
	            viewHolder = new ViewHolder();
	            viewHolder.p_download = (ProgressBar) convertView.findViewById(R.id.p_download);
	            viewHolder.download_filename = (TextView) convertView.findViewById(R.id.tv_download_filename);
	            viewHolder.tv_download_play = (TextView) convertView.findViewById(R.id.tv_download_play);
	            viewHolder.tv_download_exit=(TextView) convertView.findViewById(R.id.tv_download_exit);
	            convertView.setTag(viewHolder);
	        }else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }
		
		
		return null;
	}


    class ViewHolder{
    	ProgressBar p_download;
        TextView download_filename;
        TextView tv_download_play;
        TextView tv_download_exit;
    }
}
