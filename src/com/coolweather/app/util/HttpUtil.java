package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(
				new Runnable() {
					
					public void run() {
						HttpURLConnection connection = null;
						try {
							URL url = new URL(address);
							connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setConnectTimeout(8000);
							connection.setReadTimeout(8000);
							InputStream inputStream = connection.getInputStream();
							BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
							StringBuilder response = new StringBuilder();
							String line;
							while ((line = bufferedReader.readLine())!= null) {
								response.append(line);
							}
							if(listener != null){
								listener.onFinish(response.toString());
							}
						} catch (Exception e) {
							if(listener!=null){
								listener.onError(e);
							}
						}finally{
							if(connection != null){
								connection.disconnect();
							}
						}
						
					}
				}
				).start();
	}
}
