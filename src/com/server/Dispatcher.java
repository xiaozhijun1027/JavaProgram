package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.server.basic.servlet.Servlet;
/**
 * 分发器：加入状态内容处理  404 505 及首页
 * 
 */
public class Dispatcher implements Runnable {
	private Socket client;
	private Request request;
	private Response response ;
	public Dispatcher(Socket client) {
		this.client = client;
		try {
			//获取请求协议
			//获取响应协议
			request =new Request(client);
			response =new Response(client);
		} catch (IOException e) {
			e.printStackTrace();
			this.release();
		}
	}
	@Override
	public void run() {	
		
		try {
			if(null== request.getUrl() || request.getUrl().equals("")) {
				InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
				byte[] bs = new byte[1024*1024];
				StringBuilder stringBuilder = new StringBuilder();
				int size = 0;
				while((size = is.read(bs)) != -1){
					stringBuilder.append(new String(bs,0,size));
				}
				response.print(stringBuilder.toString());
				response.pushToBrowser(200);
				is.close();
				return ;
			}
			Servlet servlet= WebApp.getServletFromUrl(request.getUrl());
			if(null!=servlet) {
				servlet.Service(request, response);
				//关注了状态码
				response.pushToBrowser(200);
			}else {
				//错误....
				InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html");
				byte[] bs = new byte[1024*1024];
				StringBuilder stringBuilder = new StringBuilder();
				int size = 0;
				while((size = is.read(bs)) != -1){
					stringBuilder.append(new String(bs,0,size));
				}
				response.print(stringBuilder.toString());
				response.pushToBrowser(404);
				is.close();
			}		
		}catch(Exception e) {
			try {
				response.println("你好我不好，我会马上好");
				response.pushToBrowser(500);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
		release();
	}
	//释放资源
	private void release() {
		try {
			client.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
