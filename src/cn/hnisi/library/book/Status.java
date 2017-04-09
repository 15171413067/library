package cn.hnisi.library.book;

public enum Status {
	Idle("0","空闲"),Reading("1","阅读中");
	
	private String key;
	private String value;
	
	private Status(String key,String value){
		this.key = key;
		this.value = value;
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
}
