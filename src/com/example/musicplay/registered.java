package com.example.musicplay;

import com.Mysq.service.Mysql;
import com.Mysq.service.Mysql.DatafiniListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class registered extends Activity implements OnClickListener{
	private EditText ed_regId,ed_regpasswd,ed_reghxb,ed_redbj,ed_regxm;
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
		tv_regfanhui=(TextView) findViewById(R.id.tv_regfanhui);
		ed_regxm=(EditText) findViewById(R.id.ed_regxm);
		tv_regfanhui.setOnClickListener(this);
		ed_reghxb=(EditText) findViewById(R.id.ed_regxb);
		ed_redbj=(EditText) findViewById(R.id.ed_regbj);
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
			passwd=ed_regpasswd.getText().toString();
			xb=ed_reghxb.getText().toString();
			bj=ed_redbj.getText().toString();
			xm=ed_regxm.getText().toString();
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
			break;
		case R.id.tv_regfanhui:
			finish();
			break;
		default:
			break;
		}
	}
}
