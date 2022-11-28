package accountDB;
import base.*;
import java.sql.*;
import java.util.*;

import API.YoutubeViews;

public class AccountDB {
	static String URL = "jdbc:sqlite:src\\\\accountdb.db";
	static Connection connection = null;
	static PreparedStatement ps = null;
	static int countDemoAcc = 0;
	static int count = 50;
	
	// ドライバのロード
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println("ドライバのロードに失敗しました");
		}
	}
	
	/**
	 * 新たなAccountを作成しDBに登録するメソッド
	 * 同じ名前が存在する際にはfalse
	 * 
	 * @param name 新しく作成するAccountのString型の名前
	 * @param myMusic 一番の音楽を表すMusic型の値
	 * @return アカウントが作成できたかを表すboolean型の値
	 */
	public static boolean insertData(String name, Music myMusic) {
		if(existName(name)) {
        	System.out.println(name + " is already exist");
        	return false;
        }
		
		String sql = "INSERT INTO account (name,myMusicName,myMusicUrl,myMusicArtist,myMusicViews,favMusicName, favMusicUrl, favMusicArtist, favMusicViews) "
				+ "VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);

            //データベースにアカウントを作成
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, myMusic.getName());
            ps.setString(3, myMusic.getUrl());
            ps.setString(4, myMusic.getArtist());
            ps.setString(5, String.valueOf(myMusic.getViews()));
            ps.setString(6, null);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.setString(9, null);
            
            
            // アカウントを作成
            ps.executeUpdate();
            connection.commit();
            ps.close();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                connection.close();
            } catch(SQLException e) {
                // クローズが失敗
                System.err.println(e);
            }
        }
        return false;
	}
	
	/**
	 * ダミーのAccountを作成しDBに登録するメソッド
	 * 同じ名前が存在する際にはfalse
	 * 
	 * @param name 新しく作成するAccountのString型の名前
	 * @param myMusic 一番の音楽を表すMusic型の値
	 * @return アカウントが作成できたかを表すboolean型の値
	 */
	public static boolean makeDemoAccount(HashSet<Music> musicSet) {		
		String sql = "INSERT INTO account (name,myMusicName,myMusicUrl,myMusicArtist,myMusicViews,favMusicName, favMusicUrl, favMusicArtist, favMusicViews) "
				+ "VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);

            String demoName = "demo" + countDemoAcc;
            countDemoAcc += 1;
            
            Account demoAcc = new Account(demoName, YoutubeViews.getRandomMusics(musicSet, count), 
            								YoutubeViews.getRandomMusic(musicSet));
            ArrayList<String> temp = new ArrayList<>(Utils.join(demoAcc.getFavMusic()));
			
            //データベースにアカウントを作成
            ps = connection.prepareStatement(sql);
            ps.setString(1, demoAcc.getName());
            ps.setString(2, demoAcc.getMyMusic().getName());
            ps.setString(3, demoAcc.getMyMusic().getUrl());
            ps.setString(4, demoAcc.getMyMusic().getArtist());
            ps.setString(5, String.valueOf(demoAcc.getMyMusic().getViews()));
            ps.setString(6, temp.get(0));
            ps.setString(7, temp.get(1));
            ps.setString(8, temp.get(2));
            ps.setString(9, temp.get(3));
            
            
            // アカウントを作成
            ps.executeUpdate();
            connection.commit();
            ps.close();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                connection.close();
            } catch(SQLException e) {
                // クローズが失敗
                System.err.println(e);
            }
        }
        return false;
	}
	
	/**
	 * テーブルを作成するメソッド
	 */
	public static void createTable() {
		try {
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // タイムアウトを30秒にセット

			// accountというテーブルがあれば削除
			// 既に存在するテーブルを作成しようとするとエラーになるため
			statement.executeUpdate("DROP TABLE IF EXISTS account");
			// accountというテーブルを作成
			statement.executeUpdate("CREATE TABLE account(name STRING , myMusicName STRING, myMusicUrl STRING, myMusicArtist STRING, myMusicViews STRING, favMusicName STRING, favMusicUrl STRING, favMusicArtist STRING, favMusicViews STRING)");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
	
	/**
	 * 名前のアカウントを探すメソッド
	 * 
	 * @param name　検索するAccountの名前を表すString型の値
	 * @return 検索結果をあらわすAccount型のArrayList
	 */
	public static ArrayList<Account> searchAccount(String name) {
		ArrayList<Account> res = new ArrayList<>();
		try {
			String sql = "SELECT * FROM account WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			ps = connection.prepareStatement(sql);
			ps.setString(1,name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				// リザルトセットを読む
				String mmn = rs.getString("myMusicName");
				String mmu = rs.getString("myMusicUrl");
				String mma = rs.getString("myMusicArtist");
				String mmv = rs.getString("myMusicViews");
				
				String fmn = rs.getString("favMusicName");
				String fmu = rs.getString("favMusicUrl");
				String fma = rs.getString("favMusicArtist");
				String fmv = rs.getString("favMusicViews");
				
				Account temp = new Account(name, Utils.split(fmn, fmu, fma, fmv), new Music(mmn, mmu, mma, Long.parseLong(mmv)));
				res.add(temp);
				
				/*
				System.out.println("------------------------------------------------------");
				System.out.println(temp);
				System.out.println("------------------------------------------------------");
				*/
				
			}
			
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
		return res;
	}
	
	/**
	 * アカウントのおすすめの音楽のリストを返す
	 * 同じ音楽をお気に入りにしているアカウントの一番の音楽をおすすめする
	 * 
	 * @param account　音楽をおすすめするアカウント
	 * @return　再生回数で昇順にソートされたおすすめの音楽のリスト
	 */
	public static ArrayList<Music> getRecom(Account account) {
		HashSet<Music> recom = new HashSet<>();
		ArrayList<Music> res = new ArrayList<>();
		try {
			String sql = "SELECT * FROM account";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			ps = connection.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				// リザルトセットを読む
				String name = rs.getString("name");
				String mmn = rs.getString("myMusicName");
				String mmu = rs.getString("myMusicUrl");
				String mma = rs.getString("myMusicArtist");
				String mmv = rs.getString("myMusicViews");
				
				String fmn = rs.getString("favMusicName");
				String fmu = rs.getString("favMusicUrl");
				String fma = rs.getString("favMusicArtist");
				String fmv = rs.getString("favMusicViews");
				
				Account temp = new Account(name, Utils.split(fmn, fmu, fma, fmv), new Music(mmn, mmu, mma, Long.parseLong(mmv)));
				
				// お気に入りの音楽が同じものを含むアカウントの一番の音楽を結果に返す
				boolean flag = false;
				for(Music favMsc : account.getFavMusic()) {
					if(temp.getFavMusic().contains(favMsc)) {
						flag = true;
						break;
					}
				}
				if(flag) {
					recom.add(temp.getMyMusic());
				}
				
			}
			
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
		
		// すでに知っている音楽を削除
		recom.removeAll(account.getFavMusic());
		recom.remove(account.getMyMusic());
		res.addAll(new ArrayList<>(recom));
		Collections.sort(res);
		return res;
	}
	
	public static Music getRandomMusic(Account account) {
		HashSet<Music> set = new HashSet<>();
		ArrayList<Music> res = new ArrayList<>();
		try {
			String sql = "SELECT * FROM account";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			ps = connection.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				// リザルトセットを読む
				String mmn = rs.getString("myMusicName");
				String mmu = rs.getString("myMusicUrl");
				String mma = rs.getString("myMusicArtist");
				String mmv = rs.getString("myMusicViews");
				
				
				Music temp = new Music(mmn, mmu, mma, Long.parseLong(mmv));
				set.add(temp);
			}
			
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
		
		// すでに知っている音楽を削除
		set.removeAll(account.getFavMusic());
		set.remove(account.getMyMusic());
		res.addAll(new ArrayList<>(set));
		Collections.shuffle(res);
		return res.get(0);
	}
	
	/**
	 * 名前のアカウントが存在するかを確認するメソッド
	 * 
	 * @param name 存在を確認するAccountの名前を表すString型の値
	 * @return　存在するかを表すboolean型の値
	 */
	public static boolean existName(String name) {
		try {
			String sql = "SELECT * FROM account WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			ps = connection.prepareStatement(sql);
			ps.setString(1,name);
			ResultSet rs = ps.executeQuery();
			
			return rs.next();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
		return false;
	}
	
	/**
	 * アカウントを削除するメソッド
	 * 
	 * @param name　削除するAccountの名前を表すString型の値
	 */
	public static void deleteAccount(String name) {
		try {
			String sql = "DELETE FROM account WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			connection.setAutoCommit(false);

			// データベースから要素を削除
			ps = connection.prepareStatement(sql);
			ps.setString(1, name);
			// アカウントを削除
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
        }
	}
	
	/**
	 * お気に入りの音楽を変更するメソッド
	 * 
	 * @param name　変更するAccountの名前を表すString型の値
	 * @param favMusic　お気に入りの音楽を保存したMusic型のHashSet
	 */
	public static void updateFavMusic(String name, HashSet<Music> favMusic) {
		try {
			String sql = "UPDATE account SET favMusicName = ?, favMusicUrl = ?, favMusicArtist = ?, favMusicViews = ? WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			ArrayList<String> temp = new ArrayList<>(Utils.join(favMusic));
			
			// 変更後のfavMusicの値をセット
		
			ps.setString(1, temp.get(0));
			ps.setString(2, temp.get(1));
			ps.setString(3, temp.get(2));
			ps.setString(4, temp.get(3));
			
			ps.setString(5, name);
			// アップデート
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
	
	/**
	 * 一番の音楽を変更するメソッド
	 * 
	 * @param name 変更するAccountの名前を表すString型の値
	 * @param myMusic 変更する一番の音楽を表すMusic型の値
	 */
	public static void updateMyMusic(String name, Music myMusic) {
		try {
			String sql = "UPDATE account SET myMusicName = ?, myMusicUrl = ?, myMusicArtist = ?, myMusicViews = ? WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			
			// 変更後のfavMusicの値をセット
			ps.setString(1, myMusic.getName());
			ps.setString(2, myMusic.getUrl());
			ps.setString(3, myMusic.getArtist());
			ps.setString(4, String.valueOf(myMusic.getViews()));
			
			ps.setString(5, name);
			// アップデート
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
	
	/**
	 * テーブルを削除するメソッド
	 */
	public static void dropTable() {
		try {
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); //タイムアウトを30秒にセット

			// accountというテーブルがあれば削除
			statement.executeUpdate("DROP TABLE IF EXISTS account");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
}
