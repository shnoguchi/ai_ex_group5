package API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.Music;
import base.Utils;

public class YoutubeViews{
	//YouTubeApiのIDから再生回数を取得
	public static String getResult(String id) {
		
		String musicUrl = "https://www.googleapis.com/youtube/v3/videos?id=" + id 
				+ "&key=ここにAPIキーを入力&part=snippet,contentDetails,statistics,status";	
	
		
		String result = "";
		JsonNode root = null;	
		try {
			URL url = new URL(musicUrl);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.connect(); // URL接続
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
			String tmp = "";

			while ((tmp = in.readLine()) != null) {
				result += tmp;
			}

			ObjectMapper mapper = new ObjectMapper();
			root = mapper.readTree(result);
			in.close();
			con.disconnect();
		}catch(Exception e) {
			e.printStackTrace();
		}

		String viewCount; 
		
		try{
			viewCount = root.get("items").get(0).get("statistics").get("viewCount").textValue();
		}catch(Exception e) {
			viewCount = "0";
		}
		
		
		
		return viewCount;
	}
	
	//URLからIDを取得
	public static String getId(String originUrl) {
		String[] id = originUrl.split("v=");
		
		if(id.length == 2) {
			return id[1];
		}
		return null;
	}
	
	/**
	 * Youtubeのurlからサムネイル画像を取得するurlを変換するメソッド
	 * 
	 * @param music
	 * @return
	 */
	public static String getImgUrl(Music music){
		String id = getId(music.getUrl());
		
		return "http://img.youtube.com/vi/" + id + "/maxresdefault.jpg";
	}
	
	/**
	 * 再生曲順でソートしたArrayListを返すメソッド
	 * @param musicSet
	 * @return
	 */
	public static ArrayList<Music> sortMusic(HashSet<Music> musicSet){
		ArrayList<Music> res = new ArrayList<>(musicSet);
		Collections.sort(res);
		return res;
	}
	
	/**
	 * HashSetからランダムなMusicをvalueの数だけ取り出す
	 * 
	 * @param musicSet
	 * @param value
	 * @return
	 */
	public static HashSet<Music> getRandomMusics(HashSet<Music> musicSet, int value){
		ArrayList<Music> temp = new ArrayList<>(musicSet);
		Collections.shuffle(temp);
		
		HashSet<Music> res = new HashSet<>();
		for(int i = 0; i < value; i++) {
			res.add(temp.get(i));
		}
		return res;
	}
	
	/**
	 * HashSetからランダムなMusicを1つ取り出す
	 * 
	 * @param musicSet
	 * @return
	 */
	public static Music getRandomMusic(HashSet<Music> musicSet) {
		ArrayList<Music> temp = new ArrayList<>(musicSet);
		Collections.shuffle(temp);
		
		return temp.get(0);
	}
}
