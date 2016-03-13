package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;
	/**
	 * 显示城市名称
	 */
	private TextView cityNameText;
	
	/**
	 * 显示发布时间
	 */
	private TextView publishText;
	
	/**
	 * 显示天气信息描述
	 */
	private TextView weatherDespText;
	
	/**
	 * 显示气温
	 */
	private TextView temp1Text;
	
	/**
	 * 显示气温
	 */
	private TextView temp2Text;
	
	/**
	 * 显示当前时间
	 */
	private TextView currentDateText;
	
	/**
	 * 显示转换城市按钮
	 */
	private Button switchCity;
	
	/**
	 * 显示更新天气按钮
	 */
	private Button refreshWeather;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		
		switchCity = (Button) findViewById(R.id.switch_city);
		
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		String county_code = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(county_code)){
			publishText.setText("同步中。。。");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(county_code);
		}else{
			//显示天气
			showWeather();
		}
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
	}
	/***
	 * 从SD卡中读取存储的数据到界面上
	 */
	private void showWeather() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(preferences.getString("city_name", ""));
		temp1Text.setText(preferences.getString("temp1", ""));
		temp2Text.setText(preferences.getString("temp2", ""));
		weatherDespText.setText(preferences.getString("weather_desp", ""));
		publishText.setText("今天 "+preferences.getString("publish_time", "")+ "发布");
		currentDateText.setText(preferences.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}

	private void queryWeatherCode(String county_code) {
		String address = "http://www.weather.com.cn/data/list3/city"+county_code+".xml";
		queryFromServer(address,"county_code");
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.switch_city:
			
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;

		case R.id.refresh_weather:
			
			publishText.setText("同步中。。。");
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String weather_code = preferences.getString("weather_code","");
			if(!TextUtils.isEmpty(weather_code)){
				queryWeatherInfo(weather_code);
			}
			break;
			
		default:
			break;
		}
	}

	//查询天气代号对应的天气
	private void queryWeatherInfo(String weather_code) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+weather_code+".html";
		queryFromServer(address,"weather_code");
	}

	/***
	 * 根据类型和地址向服务器查询天气代号或者天气信息
	 * @param address
	 * @param string
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			public void onFinish(String response) {
				if("county_code".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] arry = response.split("\\|");
						if(arry!= null && arry.length == 2){
							String weather_code = arry[1];
							queryWeatherInfo(weather_code);
						}
						
					}
				}else if("weather_code".equals(type)){
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					public void run() {
						publishText.setText("同步失败！");
					}
				});
			}
		});
	}

}
