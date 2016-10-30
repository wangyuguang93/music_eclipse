package com.Mysq.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.musicplay.MainActivity;

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
	boolean flag = false;
	private String xingming,xb,bj;
	
	public static interface DatafiniListener{
		void datafinish(String data,String xingming,String xb,String bj);
		
		
		
	}
	DatafiniListener datafiniListener;
	public void setDatafiniListener(DatafiniListener datafiniListener){
		this.datafiniListener=datafiniListener;
	}
	
	public Mysql(String username,String userpassword) {
		// TODO Auto-generated constructor stub
		this.username=username;
		this.userpassword=userpassword;
	}
		@Override
		protected String doInBackground(Void... params) {			
			// TODO Auto-generated method stub
			
			try{
				String url = "jdbc:mysql://119.29.168.172:3306/stu2015";				
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url, "root", "yu9655"); 
				ps=conn.prepareStatement("select * from User");
				rs = ps.executeQuery();
				while(rs.next()){
					String name = rs.getString("学号");
					String pass = rs.getString("密码");
					Log.d("name", name);
					Log.d("username", username);
					Log.d("pass",pass);
					Log.d("username", userpassword);
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
				e.printStackTrace(); 
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
				else {
					Log.d("结果", "登录失败");
				}
			}
			
	} 

