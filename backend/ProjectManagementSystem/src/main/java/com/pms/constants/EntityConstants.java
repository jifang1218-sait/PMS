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
    
//    static final String kDefaultTaskName = "__DEFAULT_TASK__";
    static final long kDefaultTaskProjectId = -1L;
    static final int kMinTaskNameLen = 3;
    static final int kMaxTaskNameLen = 255;
    
    static final int kMinCommentTitleLen = 3;
    static final int kMaxCommentTitleLen = 255;
    
    static final int kMinUserNameLen = 3;
    static final int kMaxUserNameLen = 255;
    static final int kMinUserPasswordLen = 8;
    static final int kMaxUserPasswordLen = 32;
}
