/**
 * 
 */
package com.pms.constants;

/**
 * @author jifang
 *
 */
public interface EntityConstants {
    static final int kMinCompanyNameLen = 3;
    static final int kMaxCompanyNameLen = 255;
    
    static final int kMinProjectNameLen = 3;
    static final int kMaxProjectNameLen = 255;

    static final long kDefaultTaskProjectId = -1L;
    static final int kMinTaskNameLen = 3;
    static final int kMaxTaskNameLen = 255;
    
    static final int kMinCommentTitleLen = 3;
    static final int kMaxCommentTitleLen = 255;
    
    static final int kMinUserNameLen = 3;
    static final int kMaxUserNameLen = 255;
    static final int kMinUserPasswordLen = 8;
    static final int kMaxUserPasswordLen = 32;
    
    static final int kMinRoleNameLen = 3;
    static final int kMaxRoleNameLen = 255;
    
    static final String kPMSSecuritySignKey = "SAIT"; 
}
