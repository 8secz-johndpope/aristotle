package com.aristotle.member;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.State;
import com.aristotle.core.service.HttpUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class LocationService {

	HttpUtil httpUtil = new HttpUtil();
	Gson gson = new Gson();
	JsonParser jsonParser = new JsonParser();
	
	LoadingCache<Long, List<Location>> cache = CacheBuilder.newBuilder()
		       .maximumSize(1000)
		       .expireAfterWrite(1, TimeUnit.DAYS)
		       .build(
		           new CacheLoader<Long, List<Location>>() {
		             public List<Location> load(Long key) throws Exception {
		            	 String data = httpUtil.getResponse("https://www.swarajabhiyan.org/api/states");
		            	 JsonObject result = jsonParser.parse(data).getAsJsonObject();
		            	 System.out.println(result.get("LocationPlugin").toString());
		            	 List<Location> states = gson.fromJson(result.get("LocationPlugin").toString(), new TypeToken<List<Location>>(){}.getType());
		               return states;
		             }
		           });
	LoadingCache<Long, List<Location>> districtCache = CacheBuilder.newBuilder()
		       .maximumSize(1000)
		       .expireAfterWrite(1, TimeUnit.DAYS)
		       .build(
		           new CacheLoader<Long, List<Location>>() {
		             public List<Location> load(Long key) throws Exception {
		            	 String data = httpUtil.getResponse("https://www.swarajabhiyan.org/api/districts/"+key);
		            	 JsonObject result = jsonParser.parse(data).getAsJsonObject();
		            	 System.out.println(result.get("LocationPlugin").toString());
		            	 List<Location> states = gson.fromJson(result.get("LocationPlugin").toString(), new TypeToken<List<Location>>(){}.getType());
		               return states;
		             }
		           });
	
	public List<Location> getAllStates(){
		try {
			return cache.get(0L);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	public List<Location> getDistrictOfState(Long stateId){
		try {
			return districtCache.get(stateId);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
