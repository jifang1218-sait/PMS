package com.pms.constants;

// must keep the order of these values, 
// as we will use them in SecurityConfig based on the order. 
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
