package controller;

import base.common.RequestMapping;

/**
 * 处理器:
 * 	负责处理业务逻辑。
 */
public class HelloController {
	
	@RequestMapping("/hello.do")
	public String hello(){
		System.out.println(
				"HelloController's hello");
		/*
		 * 返回视图名。
		 * DispatcherServlet会依据视图名定位到
		 * 具体的jsp页面("/WEB-INF/" + 视图名+".jsp")。
		 */
		return "hello";
	}
	
}
