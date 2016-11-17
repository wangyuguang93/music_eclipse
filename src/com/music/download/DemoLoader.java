package com.music.download;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


public class DemoLoader {
private static DemoLoader loader = new DemoLoader();
private static int threadCount = 3;


private DemoLoader() {


}

// 单例设计模式
public static DemoLoader getInstance() {
return loader;
}


public void downFile(String path) {
// 去服务器端获取文件的长度,在本地创建一个跟服务器一样大小的文件
try {
URL url = new URL(path);
HttpURLConnection connection = (HttpURLConnection) url
.openConnection();
connection.setDoInput(true);
connection.setRequestMethod("GET");
connection.setReadTimeout(5000);
int code = connection.getResponseCode();
if (code == 200) {
// 获取服务器端文件的长度
int fileLength = connection.getContentLength();
// 本地创建一个跟服务器一样大小的文件
RandomAccessFile raf = new RandomAccessFile("setup.ext", "rwd");
raf.setLength(fileLength);
raf.close();
// 假设三个线程下载
int blockSize = fileLength / threadCount;
for (int threadId = 0; threadId < threadCount; threadId++) {
int startIndex = (threadId - 1) * blockSize;
int endIndex = threadId * blockSize - 1;
if (threadId == threadCount) {
endIndex = fileLength;
}
System.out.println("线程：" + threadId + ",下载：" + startIndex
+ "--->" + endIndex);
// 开始下载
new DownLoadThread(threadId, startIndex, endIndex, path)
.start();
}
System.out.println("文件总长度为：" + fileLength);
} else {
System.out.println("请求失败！");
}


} catch (Exception e) {
e.printStackTrace();
}
}


/**
* 下载文件的主线程
* 
* @author Administrator zengtao
* 
*/
public class DownLoadThread extends Thread {
private int threadId;
private int startIndex;
private int endIndex;
private String path;


/**
* 
* @param threadId
* 线程id
* @param startIndex
* 线程下载开始位置
* @param endIndex
* 线程下载结束位置
* @param path
* 线程下载结束文件放置地址
*/
public DownLoadThread(int threadId, int startIndex, int endIndex,
String path) {
super();
this.threadId = threadId;
this.startIndex = startIndex;
this.endIndex = endIndex;
this.path = path;
}


@Override
public void run() {
super.run();
URL url;
try {
url = new URL(path);
HttpURLConnection connection = (HttpURLConnection) url
.openConnection();
// 请求服务器下载部分的文件，制定开始的位置，和结束位置
connection.setRequestProperty("Range", "bytes=" + startIndex
+ "-" + endIndex);
connection.setDoInput(true);
connection.setRequestMethod("GET");
connection.setReadTimeout(5000);
// 从服务器获取的全部数据，返回：200，从服务器获取部分数据，返回：206
int code = connection.getResponseCode();
System.out.println("code = " + code);
InputStream is = connection.getInputStream();
RandomAccessFile raf = new RandomAccessFile("setup.exe", "rwd");
// 随机写文件的时候，从什么时候开始
raf.seek(startIndex);
int len = 0;
byte[] buff = new byte[1024];
while ((len = is.read(buff)) != -1) {
raf.write(buff, 0, len);
}
is.close();
raf.close();
System.out.println("线程：" + threadId + ",下载完成");
} catch (Exception e) {
e.printStackTrace();
}
}
}
}