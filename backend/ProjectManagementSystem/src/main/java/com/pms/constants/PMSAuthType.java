package com.pms.constants;

public enum PMSAuthType {
	system("system"), 
	microsoft("microsoft"), 
	google("google");
	
	private String value;
	 
	PMSAuthType(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
