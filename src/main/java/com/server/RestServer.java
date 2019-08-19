package com.server;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.Student;

import spark.Spark;

public class RestServer<T> {
	
	private static ObjectMapper mapper = new ObjectMapper();
	private HashMap<String, T> store = new HashMap<>();
	private int storeSize;
	private Class<T> model;
	
	public static <M> void start(Class<M> model){
		RestServer<M> rs = new RestServer<>();
		rs.model = model;
	    String basePath = "/"+model.getSimpleName().toLowerCase();
	    rs.get(basePath);
	    rs.post(basePath);
	}
	
	private void get(String basePath){
		System.out.println("API GET - "+basePath);
		Spark.get(basePath, (req, res) -> {
			String id = req.queryParams("id");
			res.header("content-type", "application/json");
			if(id!=null){
				return mapper.writeValueAsString(store.get(id));
			}else{
				return mapper.writeValueAsString(store.values());
			}
	    	 
	    	   /*return mapper.writeValueAsString(Student.builder()
	    			                           .name("Ravi narayan")
	    			                           .age(25)
	    			                           .grade("B.tech")
	    			                           .build());*/
	    	});
	}
	
    private void post(String basePath){
    	System.out.println("API POST - "+basePath);
    	Spark.post(basePath, (req, res) -> {
    		    System.out.println("body:"+req.body());
    		    res.header("content-type", "application/json");
	    		T obj = mapper.readValue(req.body(), model);
	    		storeSize = storeSize+1;
	    		String id = String.valueOf(storeSize);
	    		store.put(id, obj);
	    		JSONObject resp = new JSONObject();
	    		resp.put("id", id);
        	    return resp.toJSONString();
        });
	}
    
	private void put(String basePath){
		System.out.println("API PUT - "+basePath);
		Spark.put(basePath, (req, res) -> {
				String id = req.queryParams("id");
				if(id==null)
					return "";
				
				if(store.containsKey(id)){
					T obj = mapper.readValue(req.body(), model);
					store.put(id, obj);
				}
		    	return "";
	    });
	}
	
	private void delete(String basePath){
		System.out.println("API DELETE - "+basePath);
		Spark.delete(basePath, (req, res) -> {
			String id = req.queryParams("id");
			if(id==null)
				return "";
			
			if(store.containsKey(id)){
				store.remove(id);
			}
	    	return "";
	    });
	}
	
}
