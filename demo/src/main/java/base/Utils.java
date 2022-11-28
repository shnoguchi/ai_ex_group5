package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import base.*;

public class Utils{
	/**
	 * カンマで区切られた文字列をリストに変換するメソッド
	 * 
	 * @param source カンマで区切られたString型の文字列
	 * @return String型のArrayList
	 */
	public static ArrayList<String> split(String source) {
		String[] splited = source.split(",");
		return new ArrayList<String>(Arrays.asList(splited));
	}
	
	/**
	 * 3つのStringからMusic型HhashSetを作成
	 * 
	 * @param name 音楽の名前をカンマで区切って保存したString型の値
	 * @param url　音楽のurlをカンマで区切って保存したString型の値
	 * @param artist 音楽のアーティストをカンマで区切って保存したString型の値
	 * @param views 音楽の再生数をカンマで区切って保存したString型の値
	 * @return Music型のHashSet
	 */
	public static HashSet<Music> split(String name, String url, String artist, String views){
		HashSet<Music> res = new HashSet<>();
		if(name == null || url == null || artist == null) return res;
		
		ArrayList<String> nameList = split(name);
		ArrayList<String> urlList = split(url);
		ArrayList<String> artistList = split(artist);
		ArrayList<String> viewsList = split(views);
		
		for(int i = 0; i < nameList.size(); i++) {
			res.add(new Music(nameList.get(i), urlList.get(i), artistList.get(i), Long.parseLong(viewsList.get(i))));
		}
		return res;
	}
	
	/**
	 * Musicの各要素を「,」で結合するメソッド
	 * 
	 * @param musicSet　結合するMusicのHashSet
	 * @return
	 */
	public static ArrayList<String> join(HashSet<Music> musicSet) {
		ArrayList<String> name = new ArrayList<>();
		ArrayList<String> url = new ArrayList<>();
		ArrayList<String> artist = new ArrayList<>();
		ArrayList<String> views = new ArrayList<>();
		
		for(Music msc : musicSet) {
			name.add(msc.getName());
			url.add(msc.getUrl());
			artist.add(msc.getArtist());
			views.add(String.valueOf(msc.getViews()));
		}
		
		String joinedName = name.stream().collect(Collectors.joining(","));
		String joinedUrl = url.stream().collect(Collectors.joining(","));
		String joinedArtist = artist.stream().collect(Collectors.joining(","));
		String joinedViews = views.stream().collect(Collectors.joining(",")); 
				
		ArrayList<String> res = new ArrayList<>();
		res.add(joinedName);
		res.add(joinedUrl);
		res.add(joinedArtist);
		res.add(joinedViews);
		
		return res;
	}
	
	public static HashSet<Music> txt2music(String path){
		HashSet<Music> musicSet = new HashSet<>();
		File file = new File(path);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String str;
			while ((str = br.readLine()) != null) {
				String[] splitted = str.split(",", 3);
				Music music = new Music(splitted[0], splitted[1], splitted[2]);
				
				musicSet.add(music);
				
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		return musicSet;
	}
	
}