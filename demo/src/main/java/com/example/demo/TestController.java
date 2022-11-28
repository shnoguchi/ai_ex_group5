package com.example.demo;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import API.YoutubeViews;
import accountDB.*;
import base.*;

@Controller
public class TestController {
	/** ログインしたアカウントを保存する値　**/
	Account focusedAccount;
	Music focusedMusic;
	ArrayList<Music> alreadyRecomed = new ArrayList<>();
	
	/**
	 * アカウントを作成するメソッド
	 * accountFormクラスをビューに追加する
	 * 
	 * @param model
	 * @return アカウントを作成するhtml
	 */
	 @GetMapping("/makeAccount")
	  public String makeAccount(Model model) {
	    model.addAttribute("account", new AccountForm());
	    return "signup.html";
	  }

	 /**
	  * アカウントを作成するメソッド
	  * 入力された値を受け取る
	  * 
	  * @param account　入力された値を保存するクラス
	  * @param model
	  * @return ログインページのhtml、アカウント作成に失敗した場合はアカウント作成を行うhtml
	  */
	  @PostMapping("/makeAccount")
	  public String makeAccount(@ModelAttribute AccountForm account, Model model) {
	    model.addAttribute("account", account);
	    Music myMusic = new Music(account.getMusicName().replace(',', ' '),
	    							account.getMusicUrl().replace(',', ' '),
	    							account.getMusicArtist().replace(',', ' '));
	    
	    Account newAccount = new Account(account.getName(), new HashSet<Music>(), myMusic);
	    System.out.println(newAccount);
	    if(AccountDB.existName(newAccount.getName())) {
	    	model.addAttribute("modelValue", "他の名前を入力してください");
	    	return "signup.html";
	    }else {
	    	AccountDB.insertData(newAccount.getName(), myMusic);
	    	return "login.html";	
	    }
	    
	  }
	  
	  /**
	   * 一番の音楽を変更するメソッド
	   * accountFormクラスをビューに追加する
	   * 
	   * @param model
	   * @return アカウントを作成するhtml
	   */
	  @GetMapping("/changeMyBest")
	  public String changeMyBest(Model model) {
		  model.addAttribute("myBest", new AccountForm());
		  return "my_best.html";
	  }

	  /**
	   * 一番の音楽を変更するメソッド
	   * 入力された値を受け取る
	   * 
	   * @param account　入力された値を保存するクラス
	   * @param model
	   * @return ログインページのhtml、アカウント作成に失敗した場合はアカウント作成を行うhtml
	   */
	  @PostMapping("/changeMyBest")
	  public String changeMyBest(@ModelAttribute AccountForm myBest, Model model) {
		  model.addAttribute("myBest", myBest);
		  
		  Music myMusic = new Music(myBest.getMusicName().replace(',', ' '),
				  					myBest.getMusicUrl().replace(',', ' '),
				  					myBest.getMusicArtist().replace(',', ' '));
		  System.out.println(myMusic);
		  AccountDB.updateMyMusic(focusedAccount.getName(), myMusic);
		  return topPage(model);

	  }
		  
	
	  /**
	   * ログインできるかを判定するメソッド
	   * 
	   * @param name
	   * @param model
	   * @return ログインに成功すればトップページに遷移,失敗すればログインページに留まる
	   */
	  @RequestMapping(value = "/canLogin")
	  private String canLogin(String name, Model model) {
		  if(AccountDB.existName(name)) {
			  focusedAccount = AccountDB.searchAccount(name).get(0);
			  return topPage(model);
		  }
		  else {
			  model.addAttribute("modelValue", "名前が存在しません");
			  return "login.html";
		  }
	  }

	  

	  /**
	   * トップページを表示するメソッド
	   * 
	   * @param model
	   * @return
	   */
	  @RequestMapping(value = "/topPage")
	  private String topPage(Model model) {
		  if(focusedAccount.getFavMusic() == null || focusedAccount.getFavMusic().size() < 10) {
			  System.out.println("rand");
			  focusedMusic = AccountDB.getRandomMusic(focusedAccount).clone();  
		  }else {
			  ArrayList<Music> recomList = AccountDB.getRecom(focusedAccount);
			  recomList.removeAll(alreadyRecomed);
			  if(recomList.size() == 0)
				  focusedMusic = AccountDB.getRandomMusic(focusedAccount).clone();
			  else
				  focusedMusic = recomList.get(0);
				  alreadyRecomed.add(focusedMusic);
		  }
		  
		  model.addAttribute("accountName", focusedAccount.getName());
		  model.addAttribute("musicName", focusedMusic.getName());
		  model.addAttribute("musicArtist", focusedMusic.getArtist());
		  model.addAttribute("musicUrl", focusedMusic.getUrl());
		  System.out.println(YoutubeViews.getImgUrl(focusedMusic));
		  model.addAttribute("musicImg", YoutubeViews.getImgUrl(focusedMusic));
		  return "home.html";
	  }
	  
	  @RequestMapping(value="/ifLiked")
	  private String ifLiked(Model model) {
		  if(focusedMusic != null) {
			  focusedAccount.addFavMusic(focusedMusic);
		  }
		  AccountDB.updateFavMusic(focusedAccount.getName(), focusedAccount.getFavMusic());
		  AccountDB.searchAccount(focusedAccount.getName());
		  System.out.println(focusedMusic);
		  System.out.println(focusedAccount.getFavMusic());
		  
		  return topPage(model);
	  }
	  
	  @RequestMapping(value="/favList")
	  private String favList(Model model) {
		  model.addAttribute("accountName", focusedAccount.getName());
		  model.addAttribute("favList", focusedAccount.getFavMusic());
		  return "/favList.html";
	  }

	  @RequestMapping(value = "/")
	  private String loginTest(@ModelAttribute("name") String name, Model model) {
		  System.out.println(name);
		  return "/login.html";
	  }
}

class AccountForm {

	  private String name;
	  private String musicName;
	  private String musicUrl;
	  private String musicArtist;

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public String getMusicName() {
	    return musicName;
	  }

	  public void setMusicName(String musicName) {
	    this.musicName = musicName;
	  }

	  public String getMusicUrl() {
		  return musicUrl;
	  }

	  public void setMusicUrl(String musicUrl) {
		  this.musicUrl = musicUrl;
	  }
	  
	  public String getMusicArtist() {
		  return musicArtist;
	  }

	  public void setMusicArtist(String musicArtist) {
		  this.musicArtist = musicArtist;
	  }
	}
