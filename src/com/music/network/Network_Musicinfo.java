package com.music.network;

import android.os.Parcel;
import android.os.Parcelable;

public class Network_Musicinfo implements Parcelable{
	private String mtitle=null;
	private String mSongId=null;
	private String mauthor=null;
	private String mting_uid=null;
	private String malbum_id=null;
	private String malbum_title=null;
	private String mlrclink=null;
	private String mpic_small=null;
	private String mSize;
	private String mMusicPath=null;
	public Network_Musicinfo() {
		// TODO Auto-generated constructor stub
	}
	
	public String getmMusicPath() {
		return mMusicPath;
	}

	public void setmMusicPath(String mMusicPath) {
		this.mMusicPath = mMusicPath;
	}

	public String getMtitle() {
		return mtitle;
	}
	public void setMtitle(String mtitle) {
		this.mtitle = mtitle;
	}
	public String getmSongId() {
		return mSongId;
	}
	public void setmSongId(String mSongId) {
		this.mSongId = mSongId;
	}
	public String getMauthor() {
		return mauthor;
	}
	public void setMauthor(String mauthor) {
		this.mauthor = mauthor;
	}
	public String getMting_uid() {
		return mting_uid;
	}
	public void setMting_uid(String mting_uid) {
		this.mting_uid = mting_uid;
	}
	public String getMalbum_id() {
		return malbum_id;
	}
	public void setMalbum_id(String malbum_id) {
		this.malbum_id = malbum_id;
	}
	public String getMalbum_title() {
		return malbum_title;
	}
	public void setMalbum_title(String malbum_title) {
		this.malbum_title = malbum_title;
	}
	public String getMlrclink() {
		return mlrclink;
	}
	public void setMlrclink(String mlrclink) {
		this.mlrclink = mlrclink;
	}
	public String getMpic_small() {
		return mpic_small;
	}
	public void setMpic_small(String mpic_small) {
		this.mpic_small = mpic_small;
	}
	public String getmSize() {
		return mSize;
	}
	public void setmSize(String mSize) {
		this.mSize = mSize;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mtitle);
		dest.writeString(mauthor);
		dest.writeString(malbum_id);
		dest.writeString(malbum_title);
		dest.writeString(mlrclink);
		dest.writeString(mSize);
		dest.writeString(mSongId);
		dest.writeString(mting_uid);
		dest.writeString(mpic_small);
		dest.writeString(mMusicPath);
		
			}
	
	 public void readFromParcel(Parcel in) {
		 malbum_id=in.readString();
		 malbum_title=in.readString();
		 mauthor=in.readString();
		 mlrclink=in.readString();
		 mpic_small=in.readString();
		 mting_uid=in.readString();
		 mtitle=in.readString();
		 mSize=in.readString();
		 mpic_small=in.readString();
		 mMusicPath=in.readString();
		 
		 }
	  public static final Parcelable.Creator<Network_Musicinfo> CREATOR = new Parcelable.Creator<Network_Musicinfo>() {
	        public Network_Musicinfo createFromParcel(Parcel in) {
	            return new Network_Musicinfo(in);
	        }

	        public Network_Musicinfo[] newArray(int size) {
	            return new Network_Musicinfo[size];
	        }

	    };

	    private Network_Musicinfo(Parcel in) {
	        readFromParcel(in);
	    }

	    
}
