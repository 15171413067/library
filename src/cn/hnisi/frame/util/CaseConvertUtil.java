package cn.hnisi.frame.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cn.hnisi.frame.exception.AppException;

public class CaseConvertUtil {
	
	private static final char SEPARATOR = '_';

	public static String underscoreToCamel(String s){
		if (s == null) {
            return null;
        }
 
        s = s.toLowerCase();
 
        StringBuilder sb = new StringBuilder(s.length());
        boolean convertCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
 
            if (c == SEPARATOR) {
            	convertCase = true;
            } else if (convertCase) {
                sb.append(Character.toUpperCase(c));
                convertCase = false;
            } else {
                sb.append(c);
            }
        }
 
        return sb.toString();
	}
	
	public static Map<String,Object> underscoreToCamel(Map<String,Object> inMap){
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		Set<Entry<String, Object>> entrySet = inMap.entrySet();
		for(Entry<String,Object> entry : entrySet){
			String key = entry.getKey();
			Object value = entry.getValue();
			String rtnKey = underscoreToCamel(key);
			rtnMap.put(rtnKey, value);
		}
		return rtnMap;
	}
	
	public static List<Map<String,Object>> underscoreToCamel(List<Map<String,Object>> inList){
		List<Map<String,Object>> rtnList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> inMap : inList){
			Map<String,Object> rtnMap = underscoreToCamel(inMap);
			rtnList.add(rtnMap);
		}
		return rtnList;
	}
	
	public static int parseInt(Object o){
		if(o == null){
			throw new AppException("传入参数为空");
		}
		Double doubleValue = Double.parseDouble(o.toString());
		return doubleValue.intValue();
	}
	
	/**
	 * 将list转换为String
	 * @param list		待转换的list
	 * @param separator	分隔符
	 * @return
	 */
	public static String listToStr(List list,String separator){
		String rtnStr = "";
		for(Object obj : list){
			if(rtnStr != ""){
				rtnStr += separator;
			}
			String tmpStr = obj.toString();
			rtnStr += tmpStr;
		}
		return rtnStr;
	}
	
	/**
	 * 将list转换为sql使用的条件字符串,格式为('str1','str2')
	 * @param list
	 * @return
	 */
	public static String listToSqlWhere(List list){
		String separator = ",";
		String rtnStr = listToStr(list,separator);
		return "("+rtnStr+")";
	}
}
