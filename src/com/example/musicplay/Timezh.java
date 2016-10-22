package com.example.musicplay;

public class Timezh{
	private int hh,mm,ss;
	private int zhtime;
	public Timezh() {
		// TODO Auto-generated constructor stub
		//this.zhtime=zhtime;
		//this.zhtime=this.zhtime/1000;
		hh=0;
		mm=0;
		ss=0;
	}
	public int HH(int hh1) {
		hh=hh1/1000/3600;
		return hh;
	}
	public int mm(int mm1) {
		HH(mm1);
		mm=(mm1/1000-hh*3600)/60;
		return mm;
	}
	public int ss(int ss1) {
		HH(ss1);
		mm(ss1);
		ss=ss1/1000-(hh*3600+mm*60);
		
		return ss;
	}
}
