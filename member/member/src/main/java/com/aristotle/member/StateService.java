package com.aristotle.member;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.aristotle.core.persistance.State;
import com.aristotle.core.service.HttpUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Component
public class StateService {

	HttpUtil httpUtil = new HttpUtil();
	Gson gson = new Gson();
	
	LoadingCache<Long, List<State>> cache = CacheBuilder.newBuilder()
		       .maximumSize(1000)
		       .expireAfterWrite(1, TimeUnit.DAYS)
		       .build(
		           new CacheLoader<Long, List<State>>() {
		             public List<State> load(Long key) throws Exception {
		            	 String data = httpUtil.getResponse("https://www.swarajabhiyan.org/api/states");
		            	 List<State> states = gson.fromJson(data, new TypeToken<List<State>>(){}.getType());
		               return states;
		             }
		           });
	
	public List<State> getAllStates(){
		try {
			return cache.get(0L);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
