package base;

import java.util.*;

public class Account {
	String name;
	Music myMusic;
	HashSet<Music> favMusic;
	
	public Account() {
		favMusic = new HashSet<>();
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param name
	 * @param favMusic
	 * @param myMusic
	 */
	public Account(String name, HashSet<Music> favMusic, Music myMusic) {
		this.name = name;
		this.favMusic = favMusic;
		this.myMusic = myMusic;
	}
	
	public String getName() {
		return this.name;
	}
	
	public HashSet<Music> getFavMusic() {
		return this.favMusic;
	}
	
	public Music getMyMusic() {
		return this.myMusic;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFavMusic(HashSet<Music> favMusic) {
		this.favMusic = favMusic;
	}
	
	public void addFavMusic(Music music) {
		this.favMusic.add(music);
	}
	
	public void setMyMusic(Music myMusic) {
		this.myMusic = myMusic;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return name + "\n" + favMusic + "\n" + myMusic;
	}
	
	/**
	 * 
	 */
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o == null) return false;
		if(!(o instanceof Account)) return false;
		Account a = (Account) o;
		if(this.name.equals(a.getName())) return true;
		
		return false;
	}
	
	/**
	 * 
	 */
	public int hashCode() {
		return Objects.hash(name, myMusic, favMusic);
	}
}
