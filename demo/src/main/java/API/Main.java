package API;

import java.util.HashSet;

import base.Music;
import base.Utils;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<Music> temp = Utils.txt2music("src/main/java/base/database_myMusic.txt");
		
		System.out.println(YoutubeViews.sortMusic(temp));
		for(int i = 0; i < 4; i++) {
			System.out.println("-----" + i + "-----");
			System.out.println(YoutubeViews.getRandomMusics(temp, 4));
		}
		
		for(int i = 0; i < 4; i++) {
			System.out.println("-----" + i + "-----");
			System.out.println(YoutubeViews.getRandomMusic(temp));
		}
	}

}
