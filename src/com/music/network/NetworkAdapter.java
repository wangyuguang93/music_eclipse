package com.music.network;

import android.content.Context;
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

import java.util.List;

import com.example.musicplay.R;
import com.music.network.BackAsyTask.Getmusic_ico;

/**
 * Created by Xiamin on 2016/8/28.
 */
public class NetworkAdapter extends BaseAdapter implements OnClickListener {
	private NetKGmusicInfo arr[];
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<NetKGmusicInfo> amusiclist;
    private Bitmap[] bitmap;
    private String[] net_pic_small;
   private int i;
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
       viewHolder.iv_more.setOnClickListener(this);
        return view;
    }

    class ViewHolder{
        ImageView image;
        TextView title;
        TextView artist;
        ImageView iv_more;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_more:
			//Toast.makeText(mContext, "点击了img", Toast.LENGTH_SHORT).show();
			 showPopupMenu(v);
			break;
			
		default:
			break;
		}
	}

	 private void showPopupMenu(View view) {
		 // View当前PopupMenu显示的相对View的位置
		 PopupMenu popupMenu = new PopupMenu(mContext, view);
		 // menu布局
		 popupMenu.getMenuInflater().inflate(R.menu.option, popupMenu.getMenu());
		 // menu的item点击事件
		 popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
		  @Override
		  public boolean onMenuItemClick(MenuItem item) {
		 // Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_SHORT).show();
			  switch (item.getItemId()) {
			case R.id.option_xiangqing:
				Toast.makeText(mContext, arr[0].getSongname(), Toast.LENGTH_SHORT).show();
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