package base.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射处理器:
 *    负责提供请求路径与处理器的对应关系。
 *    比如，"/hello.do"这个请求应该由
 *    HelloController的hello方法来处理。
 *
 */
public class HandlerMapping {
	
	//handlerMap用于存放请求路径与处理器的对应关系。
	private Map<String,Handler> handlerMap = 
			new HashMap<String,Handler>();
	
	/**
	 * 依据请求路径返回Handler对象。
	 *   注:
	 *   	Handler对象封装了处理器及Method对象。
	 */
	public Handler getHandler(String path){
		return handlerMap.get(path);
	}
	
	
	/**
	 * 负责建立请求路径与Handler的对应关系。
	 * 首先遍历beans集合，然后利用java反射读取
	 * 处理器中的@RequestMapping的请求路径。
	 * 接下来，以请求路径作为key,以Handler作为
	 * value(Handler封装了处理器及Method对象),
	 * 将这个对应关系存放到handlerMap里面。
	 */
	public void process(List beans) {
		for(Object bean : beans){
			Class clazz = bean.getClass();
			//获得处理器的所有方法
			Method[] methods = 
					clazz.getDeclaredMethods();
			//遍历处理器的所有方法
			for(Method mh : methods){
				//获得加在方法前的@RequestMapping注解
				RequestMapping rm = 
					mh.getDeclaredAnnotation(
							RequestMapping.class);
				//读取@RequestMapping注解的value属性值
				//(该属性值是请求路径)
				String path = rm.value();
				System.out.println("path:" + path);
				//以请求路径作为key,以Handler对象作为 
				//value(该对象封装了Method及处理器实例),
				//将对应关系存放到handlerMap。
				handlerMap.put(path, 
						new Handler(mh,bean));
			}
			
		}
		System.out.println("handlerMap:" 
				+ handlerMap);
		
		
		
	}

}

