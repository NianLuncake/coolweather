package com.coolweather.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /*
    * 解析和处理服务器返回的数据
    */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for(int i = 0;i<allProvinces.length();i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return true;
    }

    /*
    * 解析和处理服务器返回的市级数据
    */
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i = 0;i<allCities.length();i++){
                    JSONObject cityObject =allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                Log.d("Choose", "handleCityResponse: 获取成功");
                return true;
            }catch (JSONException e){
                e.printStackTrace();
                Log.d("Choose", "获取异常");
            }
        }return false;
        }

        /*
        * 解析和处理服务器返回的县级数据
        */
        public static boolean handleCountyResponse(String response , int cityId){
            if(!TextUtils.isEmpty(response)){
                try{
                    JSONArray allCounties = new JSONArray(response);
                    for(int i = 0;i<allCounties.length();i++){
                        JSONObject countyObject =allCounties.getJSONObject(i);
                        County county = new County();
                        county.setCityId(cityId);
                        county.setCountyName(countyObject.getString("name"));
                        county.setWeatherId(countyObject.getString("weather_id"));
                        county.save();
                    }
                    return true;
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return false;
        }
        /*
         * 将返回的json数据解析成weather对象
         * */
        public static Weather handleWeatherResponse(String response){
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
                String weatherContent = jsonArray.getJSONObject(0).toString();
                Log.d("Utility", "handleWeatherResponse: success");
                return new Gson().fromJson(weatherContent, Weather.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("Utility", "handleWeatherResponse:false ");
            return null;
        }
    }


