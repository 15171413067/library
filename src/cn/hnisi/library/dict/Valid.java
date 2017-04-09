package cn.hnisi.library.dict;

import cn.hnisi.frame.exception.AppException;

public enum Valid {
	TRUE("1","有效"),FALSE("0","无效");
	
	private String key;
	private String value;
	
	private Valid(String key,String value){
		this.setKey(key);
		this.setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static Valid byValue(String value){
		for(Valid valid : Valid.values()){
			if(valid.getValue().equals(value)){
				return valid;
			}
		}
		throw new AppException("字典项Valid不存在value="+value);
	}
	
	public static Valid byKey(String key){
		for(Valid valid : Valid.values()){
			if(valid.getKey().equals(key)){
				return valid;
			}
		}
		throw new AppException("字典项Valid不存在key="+key);
	}
	
}
