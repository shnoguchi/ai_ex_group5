package base;

import java.util.Objects;

import API.YoutubeViews;

public class Music implements Comparable<Music>{
	String name;
	String artist;
	String url;
	long views;
	
	public Music() {
		this.name = "NONE";
		this.artist = "NONE";
		this.url = "NONE";
	}
	
	public Music(String name, String url, String artist) {
		this.name = name;
		this.url = url;
		this.artist = artist;
		
		String id = YoutubeViews.getId(url);
		String strViews = YoutubeViews.getResult(id);
		this.views = Long.parseLong(strViews);
	}
	
	public Music(String name, String url, String artist, long views) {
		this.name = name;
		this.url = url;
		this.artist = artist;
		this.views = views;
	}
	
	public String getName() {
		return this.name;
	}

	public String getArtist() {
		return this.artist;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getImg() {
		return YoutubeViews.getImgUrl(this);
	}
	
	public long getViews() {
		return this.views;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setViews(long views) {
		this.views = views;
	}
	
	/**
	 * 同値関係を定義するメソッド
	 * すべてのインスタンス変数が同値であればtrue
	 * 
	 * @param o 比較対象のオブジェクト
	 */
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o == null) return false;
		if(!(o instanceof Music)) return false;
		Music r = (Music) o;
		if(this.getName().equals(r.getName()) && 
			this.getArtist().equals(r.getArtist()) &&
			this.getUrl().equals(r.getUrl())) return true;
		
		return false;
	}
	
	/**
	 * 自分自身のコピーを作成するメソッド
	 */
	public Music clone() {
		Music clonedMusic = new Music();
		clonedMusic.setName(this.getName());
		clonedMusic.setArtist(this.getArtist());
		clonedMusic.setUrl(this.getUrl());
		clonedMusic.setViews(this.getViews());
		return clonedMusic;
	}
	
	/**
	 * ハッシュ値を計算するメソッド
	 */
	public int hashCode() {
		return Objects.hash(this.name, this.url, this.artist);
	}
	
	public String toString() {
		return name + " by " + artist + " URL : " + url + " Views : " + views;
	}
	
	public int compareTo(Music obj) {
		if(this.views < obj.getViews()) {
			return -1;
		}
		
		if(this.views > obj.getViews()) {
			return 1;
		}
		
		return 0;
	}
}
