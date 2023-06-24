/**
 * 
 */
package com.pms;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.pms.entities.IEntityPath;

/**
 * @author jifang
 *
 */
@Configuration
@ComponentScan(basePackages = {"com.pms"})
@EntityScan(basePackageClasses=IEntityPath.class)
public class JavaConfig {
    
}
