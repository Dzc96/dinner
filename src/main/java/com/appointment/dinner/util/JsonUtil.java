package com.appointment.dinner.util;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Description : json工具类
*/
public class JsonUtil {

	private static final ObjectMapper mapper;
	
	//TODO 待优化
	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private JsonUtil() {
			super();
		}

		public static <T> T jsonToObject(String json, Class<T> clazz) {
			T object = null;
			try {
				object = mapper.readValue(json, clazz);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return object;
	}
	
	public static <T> String objectToJson(T object) {
		String json = null;
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		List<T> list = null;
		try {
			CollectionLikeType collectionType = mapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, clazz);
			list = mapper.readValue(json, collectionType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static <K,V> Map<K,V> jsonToMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
		Map<K,V> map = null;
		try {
			MapLikeType mapType = mapper.getTypeFactory().constructMapLikeType(HashMap.class, keyClazz, valueClazz);
			map = mapper.readValue(json, mapType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
