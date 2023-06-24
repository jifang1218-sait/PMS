package com.pms.constants;

public enum PMSRoleName {
	admin("admin"), 
	manager("manager"), 
	technician("technician"), 
	viewer("viewer");
	
	private String value;
	 
	PMSRoleName(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
