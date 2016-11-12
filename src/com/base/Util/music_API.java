package com.base.Util;

public interface music_API {
	static String musicapi1="http://tingapi.ting.baidu.com//v1/restserver/ting?from=android&version=5.9.0.0&channel=webapphomebanner&operator=1&method=baidu.ting.search.merge&format=xml&query="; 
	static String Kgmusic="http://mobilecdn.kugou.com/api/v3/search/song?format=json&page=1&pagesize=3&keyword=";
	static String Kglyc="http://lyrics.kugou.com/search?ver=1&man=yes&client=pc";
	static String Kglycdownload="http://lyrics.kugou.com/download?ver=1&client=pc&fmt=lrc&charset=utf8";
	static String KgmusicInfo="http://m.kugou.com/app/i/getSongInfo.php?&cmd=playInfo&hash=";
}
