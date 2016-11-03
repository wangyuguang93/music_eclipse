package com.Mysq.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.InvalidPreferencesFormatException;

import com.example.musicplay.MainActivity;
import com.example.musicplay.R.id;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
public class Mysql extends AsyncTask<Void, Void, String>{
	Connection conn = null;  
	PreparedStatement ps = null;  
	ResultSet rs = null;  
	private String username;  
	private String userpassword;
	private Context context;
	boolean flag = false;
	private String xingming,xb,bj;
	
	public static interface DatafiniListener{
		void datafinish(String data,String xingming,String xb,String bj);
		
		
		
	}
	DatafiniListener datafiniListener;
	public void setDatafiniListener(DatafiniListener datafiniListener){
		this.datafiniListener=datafiniListener;
	}
	
	public Mysql(String username,String userpassword,Context context) {
		// TODO Auto-generated constructor stub
		this.username=username;
		this.userpassword=userpassword;
		this.context=context;
	}
	public Mysql() {
		// TODO Auto-generated constructor stub
		
	}
		@Override
		protected String doInBackground(Void... params) {			
			// TODO Auto-generated method stub
			
			try{
				//String url = "jdbc:mysql://119.29.168.172:3306/stu2015";
				String url = "jdbc:mysql://119.29.168.172:3306/stu2015?useUnicode=true&amp;characterEncoding=UTF-8";
				Class.forName("com.mysql.jdbc.Driver");
				//String sql="insert into test2 values ('"+username+"','"+userpassword+"')";
				//String sql1="select * from User where '学号'='"+username+"'and '密码'='"+username+"'";
				conn = DriverManager.getConnection(url, "root", "yu9655"); 
				//ps=conn.prepareStatement("select * from test2");
				ps=conn.prepareStatement("select * from User");
				//ps.execute();				
				rs = ps.executeQuery();
				while(rs.next()){
					String name = rs.getString("学号");
					String pass = rs.getString("密码");
//					Log.d("name", name);
//					Log.d("username", username);
//					Log.d("pass",pass);
//					Log.d("username", userpassword);
					if(username.equals(name)&&userpassword.equals(pass)){//判断用户名和密码是否正确
						System.out.println("读取成功");
						//textView.setText(username +"密码" + userpassword);
						xingming=rs.getString("姓名");
						xb=rs.getString("系部名称");
						bj=rs.getString("班级名称");
						return "ok";
						
					}
				}
				return "fale";
			}catch (SQLException e) {
				// TODO: handle exception
				System.out.println("连接Mysql数据库失败！");
				String err=e.getMessage();
				if (err.equals("Duplicate entry '"+username+"' for key 'PRIMARY'")) {
					//Log.d("用户名", "用户已存在");
					return "用户已存在";
				}
				Log.d("err", err);
				return "错误";
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "错误";
			}finally{ 
				close();
				
			}
		}
			public  String close(){
				try{
					if (rs!=null) {
						rs.close();
						rs=null;
					}
					if (ps!=null) {
						ps.close();
						ps=null;
						
					}
					if (conn!=null) {
						conn.close();
						conn=null;
					}
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println("数据库close异常"); 
				}
			
			return "错误";
		}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				datafiniListener.datafinish(result,xingming,xb,bj);
				if (result.equals("ok")) {
					Log.d("结果", "登录成功");
				}
				if(result.equals("用户已存在")){
					Toast.makeText(context, "用户已存在", Toast.LENGTH_SHORT).show();
				}
			}
			
	} 

