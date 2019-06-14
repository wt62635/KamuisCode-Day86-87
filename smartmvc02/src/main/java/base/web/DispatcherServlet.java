package base.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import base.common.Handler;
import base.common.HandlerMapping;


public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HandlerMapping handlerMapping;
	
	public void init() 
			throws ServletException {
		/*
		 * 读取smartmvc配置文件中处理器的配置信息，
		 * 然后利用java反射将处理器实例化。
		 */
		SAXReader reader = new SAXReader();
		InputStream in = 
				getClass().getClassLoader()
				.getResourceAsStream(
						"smartmvc.xml");	
		try {
			Document doc = 
					reader.read(in);
			Element root = 
					doc.getRootElement();
			List<Element> elements = 
					root.elements();
			//beans用来存放处理器实例
			List beans = new ArrayList();
			for(Element ele : elements){
				//读取处理器类名
				String className = 
					ele.attributeValue("class");
				System.out.println(
					"className:" + className);
				//将处理器实例化
				Object bean = 
					Class.forName(className)
					.newInstance();
				beans.add(bean);
			}
			System.out.println("beans:" + beans);
			
			//创建映射处理器实例
			handlerMapping = new HandlerMapping();
			/*
			 * 调用映射处理器的process方法,该方法
			 * 利用java反射读取@RequestMapping中
			 * 的请求路径，然后建立请求路径与处理器
			 * 的对应关系。
			 */
			handlerMapping.process(beans);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void service(
			HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException,
					IOException {
		/*
		 * 先获得请求资源路径，然后截取请求资源路径
		 * 的一部分生成请求路径(path),接下来，
		 * 调用HandlerMapping的getHandler方法来
		 * 获得Handler对象。最后利用Handler对象
		 * 来调用处理器的方法。
		 */
		//获得请求资源路径
		String uri = request.getRequestURI();
		System.out.println("uri:" + uri);
		//获得应用名
		String contextPath = 
				request.getContextPath();
		System.out.println("contextPath:"
				+ contextPath);
		//截取请求资源路径的一部分(除掉应用名)，
		//生成请求路径。
		String path = 
				uri.substring(
						contextPath.length());
		System.out.println("path:" + path);
		
		//依据请求路径找到对应的Handler对象。
		Handler handler = 
				handlerMapping.getHandler(path);
		System.out.println("handler:" + handler);
		
		//利用handler对象，调用处理器的方法
		Method mh = handler.getMh();
		Object bean = handler.getObj();
		//rv是处理器方法的返回值(即视图名)。
		Object rv = null;
		try {
			//调用处理器的方法
			rv = mh.invoke(bean);
			System.out.println("rv:" + rv);
			/*
			 * 依据视图名，生成jsp地址
			 * ("/WEB-INF/" + 视图名 + ".jsp")
			 */
			String jspPath = 
					"/WEB-INF/" + rv + ".jsp";
			//转发
			request.getRequestDispatcher(
					jspPath)
			.forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
	}

}
