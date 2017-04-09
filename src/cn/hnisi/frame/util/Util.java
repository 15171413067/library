package cn.hnisi.frame.util;

import java.util.Collection;

public class Util {

	public static boolean isEmpty(Object obj){
		if(obj == null){
			return true;
		}else{
			if(obj instanceof String){
				String str = (String) obj;
				return isEmpty(str);
			}else if(obj instanceof Collection){
				Collection collection = (Collection) obj;
				return isEmpty(collection);
			}
			
			return false;
		}
	}
	
	private static boolean _isEmpty(String str){
		if(str == null){
			return true;
		}else{
			return str.equals("");
		}
	}
	
	public static boolean isEmpty(String str){
		return isEmpty(str,true);
	}
	
	public static boolean isEmpty(String str,boolean ingoreSpace){
		if(str == null){
			return true;
		}else if(ingoreSpace){
			str = str.trim();
			return _isEmpty(str);
		}else{
			return _isEmpty(str);
		}
	}
	
	public static boolean isEmpty(Collection collection){
		if(collection == null){
			return true;
		}else{
			return collection.isEmpty();
		}
	}
	
	public static String nvl(String str,String replace){
		if(isEmpty(str)){
			return replace;
		}else{
			return str;
		}
	}
	
}
