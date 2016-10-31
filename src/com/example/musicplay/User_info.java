package com.example.musicplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.Mysq.service.Mysql;
import com.Mysq.service.Mysql.DatafiniListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class User_info extends Activity implements OnClickListener{
	private TextView tv_id,tv_xingming,tv_xb,tv_bj,fan_exit;
	private Button img_set_touxiang,btn_exit;
	private String xuehao,xm,xb,bj;
	private LeftFragment fragment;
	private ImageView headImage;
	
	/* 头像文件 */
	private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

	/* 请求识别码 */
	private static final int CODE_GALLERY_REQUEST = 0xa0;
	private static final int CODE_CAMERA_REQUEST = 0xa1;
	private static final int CODE_RESULT_REQUEST = 0xa2;

	// 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
	private static int output_X = 480;
	private static int output_Y = 480;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);		
		findId();
		readinfo();
		setlistent();
		set_head();
		
	}
	private void set_head() {
		// TODO Auto-generated method stub
		String localIconNormal = xuehao; 
		 try{
		FileInputStream localStream = openFileInput(localIconNormal); 
		 
		Bitmap bitmap = BitmapFactory.decodeStream(localStream);
		if (bitmap!=null) {
			//Log.d("set_head","ok1");
			headImage.setImageBitmap(bitmap);
		}
		
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
	}
	private void setlistent() {
		// TODO Auto-generated method stub
			btn_exit.setOnClickListener(this);
			fan_exit.setOnClickListener(this);
			img_set_touxiang.setOnClickListener(this);
	}

	private void readinfo() {
		// TODO Auto-generated method stub
		Log.d("xinxi","ok");
		SharedPreferences info=getSharedPreferences("info", Context.MODE_PRIVATE);
		xuehao=info.getString("xuehao", null);
		xm=info.getString("xingming", null);
		xb=info.getString("xb", null);
		bj=info.getString("bj", null);
		tv_id.setText("学号："+xuehao);
		tv_xingming.setText("姓名："+xm);
		tv_xb.setText("系部："+xb);
		tv_bj.setText("班级："+bj);
	}
	
	private void findId() {
		// TODO Auto-generated method stub
		tv_id=(TextView)findViewById(R.id.tv_id);
		tv_xingming=(TextView)findViewById(R.id.tv_xingming);
		tv_xb=(TextView)findViewById(R.id.tv_xb);
		tv_bj=(TextView) findViewById(R.id.tv_bj);
		img_set_touxiang=(Button)findViewById(R.id.img_set_touxing);
		btn_exit=(Button)findViewById(R.id.btn_exit);
		fan_exit=(TextView) findViewById(R.id.fan_exit);
		headImage = (ImageView) findViewById(R.id.img_head);
				
				// TODO Auto-generated method stub
				
			
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_exit:
			SharedPreferences info=getSharedPreferences("info", Context.MODE_PRIVATE);
    		Editor editor=info.edit();
    		editor.putBoolean("is_denglu", false);
    		editor.commit();
			finish();
			break;
		case R.id.fan_exit:
			finish();
			break;
		case R.id.img_set_touxing:
			choseHeadImageFromGallery();
			break;
		default:
			break;
		}
	}
	// 从本地相册选取图片作为头像
		private void choseHeadImageFromGallery() {
			Log.d("head", "ok");
			Intent intentFromGallery = new Intent();
			// 设置文件类型
			intentFromGallery.setType("image/*");
			intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent intent) {

			// 用户没有进行有效的设置操作，返回
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
				return;
			}

			switch (requestCode) {
			case CODE_GALLERY_REQUEST:
				cropRawPhoto(intent.getData());
				break;

			case CODE_CAMERA_REQUEST:
				if (hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory(),
							IMAGE_FILE_NAME);
					cropRawPhoto(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
							.show();
				}

				break;

			case CODE_RESULT_REQUEST:
				if (intent != null) {
					setImageToHeadView(intent);
				}

				break;
			}

			super.onActivityResult(requestCode, resultCode, intent);
		}

		/**
		 * 裁剪原始的图片
		 */
		public void cropRawPhoto(Uri uri) {

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");

			// 设置裁剪
			intent.putExtra("crop", "true");

			// aspectX , aspectY :宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);

			// outputX , outputY : 裁剪图片宽高
			intent.putExtra("outputX", output_X);
			intent.putExtra("outputY", output_Y);
			intent.putExtra("return-data", true);

			startActivityForResult(intent, CODE_RESULT_REQUEST);
		}

		/**
		 * 提取保存裁剪之后的图片数据，并设置头像部分的View
		 */
		private void setImageToHeadView(Intent intent) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				headImage.setImageBitmap(photo);
				try{
				FileOutputStream localFileOutputStream1 = openFileOutput(xuehao, 0);

				Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.JPEG;

				photo.compress(localCompressFormat, 100, localFileOutputStream1);

				localFileOutputStream1.close();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			}
		
		}
		public static boolean hasSdcard() {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				// 有存储的SDCard
				return true;
			} else {
				return false;
			}
		}

}
 