/**
 * 
 */
package com.pms;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.pms.entities.IEntityPath;
import com.pms.repositories.IPMSFileManager;
import com.pms.repositories.PMSFileManagerImpl;

/**
 * @author jifang
 *
 */
@Configuration
@ComponentScan(basePackages = {"com.pms"})
@EntityScan(basePackageClasses=IEntityPath.class)
public class JavaConfig {
    
    @Bean 
    IPMSFileManager fileManager() {
    	return new PMSFileManagerImpl();
    }
}
