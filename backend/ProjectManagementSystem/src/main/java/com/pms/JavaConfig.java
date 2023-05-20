/**
 * 
 */
package com.pms;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.pms.controllers.PMSEntityProvider;

/**
 * @author jifang
 *
 */
@Configuration
public class JavaConfig {

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public PMSEntityProvider createEntityProvider() {
        return new PMSEntityProvider();
    }
}
