package com.example.musicplay;

import com.Mysq.service.Mysql;
import com.Mysq.service.Mysql.DatafiniListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener{
	private View fanhui;
	private View login,zhuce;
	private EditText username,userpasswd;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		fanhui=findViewById(R.id.fanhui);
		login=findViewById(R.id.btn_login);
		zhuce=findViewById(R.id.btn_zhuce);
		progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
		progressBar.setIndeterminate(true);
		progressBar.bringToFront();
		progressBar.setVisibility(View.INVISIBLE);
		username=(EditText) findViewById(R.id.ed_userName);
		userpasswd=(EditText) findViewById(R.id.ed_userPasswd);
		fanhui.setOnClickListener(this);
		login.setOnClickListener(this);
		zhuce.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fanhui:
			finish();
			break;
	case R.id.btn_login:
//			Log.d("登录", "正在登录");
//			 Intent intent = new Intent();  
//             intent.putExtra("userName", "wangyuguang");  
//             //通过Intent对象返回结果，调用setResult方法  
//             setResult(1,intent);  
//             finish();//结束当前的activity的生命周期  
			progressBar.setVisibility(View.VISIBLE);
			final String name=username.getText().toString();
			String passwd=userpasswd.getText().toString();
			Mysql mysql=new Mysql(name,passwd,this);
			mysql.execute();
			mysql.setDatafiniListener(new DatafiniListener() {
				
				@Override
				public void datafinish(String data,String xingming,String xb,String bj) {
					// TODO Auto-generated method stub
					if (data.equals("ok")) {
						Toast.makeText(Login.this, "登录成功", 1000).show();
						 Intent intent = new Intent();  
			             intent.putExtra("userName",name); 
			             intent.putExtra("xingming", xingming); 
			             intent.putExtra("xb", xb); 
			             intent.putExtra("bj",bj); 
			             //通过Intent对象返回结果，调用setResult方法  
			             setResult(1,intent);  
						finish();
					}
					else {
						Toast.makeText(Login.this, "用户名或密码错误", 1000).show();
						progressBar.setVisibility(View.INVISIBLE);
					}
				}
			});
			break;
	case R.id.btn_zhuce:
		
		break;

		default:
			break;
		}
	}

}
