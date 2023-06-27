/**
 * 
 */
package com.pms.constants;

/**
 * @author jifang
 *
 */
public interface PMSEntityConstants {
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
    
    static final String kPMSSecuritySignKey = "1301, 16 Ave NW Calgary, Alberta T2M 0L4, Canada"; 
    
    // the session timeout in milliseconds, 30 minutes
    static final long kSessionTimeout = 1000 * 60 * 30;
    
    // default company id is used for user with admin role. 
    static final long kDefaultCompanyId = -1L;
    static final String kCompanyDefaultAvatarPath = "/upload/avatar/company_default_avatar.png";
    static final String kProjectDefaultAvatarPath = "/upload/avatar/project_default_avatar.png";
}
