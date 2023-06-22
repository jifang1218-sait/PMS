package com.pms.constants;

public enum PMSRoleType {
	Admin("admin"), 
	Manager("manager"), 
	Technician("technician"), 
	Viewer("viewer");
	
	private String value;
	 
	PMSRoleType(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
