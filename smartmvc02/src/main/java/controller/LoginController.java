package controller;

import base.common.RequestMapping;

public class LoginController {
	@RequestMapping("/toLogin.do")
	public String toLogin(){
		System.out.println(
				"LoginController's toLogin");
		return "login";
	}
	
	@RequestMapping("/login.do")
	public String login(){
		System.out.println(
				"LoginController's login");
		return "success";
	}
}




