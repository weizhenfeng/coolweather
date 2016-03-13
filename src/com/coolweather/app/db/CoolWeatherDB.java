package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class CoolWeatherDB {

	public static final String DB_NAME = "cool_weather";
	
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;

	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * ��ȡcoolWeatherDBʵ��
	 * @param context
	 * @return
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 * ����ʡ���ݿ�
	 * @param province
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("province_name", province.getProvinceName());
			contentValues.put("province_code", province.getProvinceCode());
			db.insert("Province", null, contentValues);
		}
	}
	/**
	 * ��ȡ����ʡ�ݵ�����
	 * @return
	 */
	public List<Province> loadProvinces(){
		List<Province> provinces = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				provinces.add(province);
				
			} while (cursor.moveToNext());
		}
		return provinces;
	}
	
	/**
	 * ����ʡ���ݿ�
	 * @param City
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("city_name", city.getCityName());
			contentValues.put("city_code", city.getCityCode());
			contentValues.put("province_id", city.getProvinceId());
			db.insert("City", null, contentValues);
		}
	}
	
	/**
	 * ��ȡ����ʡ�ݵ�����
	 * @return
	 */
	public List<City> loadCitys(int provinceId){
		List<City> citys = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				citys.add(city);
				
			} while (cursor.moveToNext());
		}
		return citys;
	}
	
	/**
	 * ����ʡ���ݿ�
	 * @param County
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("county_name", county.getCountyName());
			contentValues.put("county_code", county.getCountyCode());
			contentValues.put("city_id", county.getCityId());
			db.insert("County", null, contentValues);
		}
	}
	
	/**
	 * ��ȡ����ʡ�ݵ�����
	 * @return
	 */
	public List<County> loadCountys(int cityId){
		List<County> countys = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				countys.add(county);
				
			} while (cursor.moveToNext());
		}
		return countys;
	}
	
}
