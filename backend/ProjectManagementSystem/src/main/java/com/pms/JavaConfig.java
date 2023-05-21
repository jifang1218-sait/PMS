/**
 * 
 */
package com.pms;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.pms.controllers.PMSEntityProvider;

import com.pms.entities.IEntityPath;

/**
 * @author jifang
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.pms"})
@EntityScan(basePackageClasses=IEntityPath.class)
public class JavaConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    PMSEntityProvider createEntityProvider() {
        return new PMSEntityProvider();
    }
}
