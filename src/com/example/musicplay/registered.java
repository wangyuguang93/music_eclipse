package com.example.musicplay;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.Mysq.service.Mysql;
import com.Mysq.service.Mysql.DatafiniListener;
import com.base.Util.AESUtils;
import com.base.Util.MD5Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class registered extends Activity implements OnClickListener{
	private EditText ed_regId,ed_regpasswd,ed_regxb,ed_redbj,ed_regxm;
	private Button btn_registered;
	private TextView tv_regisnull,tv_regfanhui;
	private String xuehao,passwd,xb,bj,xm;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registered);
		ed_regId=(EditText) findViewById(R.id.ed_regId);
		ed_regpasswd=(EditText) findViewById(R.id.ed_regpasswd);		
		ed_regxb=(EditText) findViewById(R.id.ed_regxb);
		ed_redbj=(EditText) findViewById(R.id.ed_regbj);
		tv_regfanhui=(TextView) findViewById(R.id.tv_regfanhui);
		ed_regxm=(EditText) findViewById(R.id.ed_regxm);
		tv_regfanhui.setOnClickListener(this);
		tv_regisnull=(TextView) findViewById(R.id.tv_regisnull);
		tv_regisnull.setVisibility(View.INVISIBLE);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setIndeterminate(true);
		progressBar.bringToFront();
		progressBar.setVisibility(View.INVISIBLE);
		btn_registered=(Button) findViewById(R.id.btn_registered);
		btn_registered.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_registered:
			progressBar.setVisibility(View.VISIBLE);
			tv_regisnull.setVisibility(View.INVISIBLE);
			xuehao=ed_regId.getText().toString();
			passwd=MD5Utils.getMD5(ed_regpasswd.getText().toString());
			String test1=AESUtils.encrypt("yu9655", ed_regpasswd.getText().toString());
			Log.d("passMd5", passwd);
			Log.d("passAES", test1);
			xb=ed_regxb.getText().toString();
			bj=ed_redbj.getText().toString();
			xm=ed_regxm.getText().toString();
			if (xuehao.equals("")||passwd.equals("")||xb.equals("")||bj.equals("")||xm.equals("")) {
				tv_regisnull.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.INVISIBLE);
			}
			else {
				
			Mysql mysql=new Mysql(xuehao, passwd, xb, bj,xm, this);
			String registered="registered";
			mysql.execute(registered);
			mysql.setDatafiniListener(new DatafiniListener() {
				
				@Override
				public void datafinish(String data, String xingming, String xb, String bj) {
					// TODO Auto-generated method stub
					if (data.equals("regok")) {
						Toast.makeText(registered.this, "注册成功,请登录", Toast.LENGTH_SHORT).show();
						finish();
					}
					else {
						progressBar.setVisibility(View.INVISIBLE);
					}
				}
			});
			}
			break;
		case R.id.tv_regfanhui:
			finish();
			break;
		default:
			break;
		}
	}
	
}
