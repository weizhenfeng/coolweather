package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {

	/***
	 * 解析省的数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces!=null && allProvinces.length >0){
				for (String p : allProvinces) {
					String[] arry = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(arry[0]);
					province.setProvinceName(arry[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/***
	 * 解析市的数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities!=null && allCities.length >0){
				for (String p : allCities) {
					String[] arry = p.split("\\|");
					City city = new City();
					city.setCityCode(arry[0]);
					city.setCityName(arry[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/***
	 * 解析县的数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties!=null && allCounties.length >0){
				for (String p : allCounties) {
					String[] arry = p.split("\\|");
					County county = new County();
					county.setCountyCode(arry[0]);
					county.setCountyName(arry[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
}
