package accountDB;

import base.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		HashSet<Music> temp = Utils.txt2music("src/main/java/base/database_myMusic.txt");
		
		Music myMsc = new Music("The Thrill is gone", 
				"https://www.youtube.com/watch?v=SgXSomPE_FY",
				"B.B. King");
		Music msc = new Music("Revolution", 
				"youtube.com/watch?v=BGLGzRXY5Bw",
				"The Beetles");
		
		HashSet<Music> set = new HashSet<>();
		set.add(msc);
		
		Account myAcc = new Account("noguchi", new HashSet<Music>(), myMsc);
		AccountDB.createTable();
		AccountDB.insertData(myAcc.getName(), myMsc);
		AccountDB.updateFavMusic("noguchi", set);
		AccountDB.searchAccount("noguchi");
		
		AccountDB.insertData("tanaka", msc);
		AccountDB.updateFavMusic("tanaka", set);
		AccountDB.searchAccount("tanaka");
		for(int i = 0; i < 500; i++) {
			AccountDB.makeDemoAccount(temp);
		}
		
		System.out.println(AccountDB.getRecom(AccountDB.searchAccount("noguchi").get(0)));
		System.out.println(AccountDB.getRandomMusic(AccountDB.searchAccount("noguchi").get(0)));
		System.out.println(AccountDB.getRandomMusic(AccountDB.searchAccount("noguchi").get(0)));
		
		/*
		AccountDB.createTable();
		
		Music myMsc = new Music("The Thrill is gone", 
				"https://www.youtube.com/watch?v=SgXSomPE_FY",
				"B.B. King");
		
		Account myAcc = new Account("noguchi", new HashSet<Music>(), myMsc);
		AccountDB.createTable();
		AccountDB.insertData(myAcc.getName(), myMsc);
		
		System.out.println(AccountDB.existName("noguchi"));
		*/
	}

}
