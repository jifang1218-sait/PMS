/**
 * 
 */
package com.pms.controllers;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import com.pms.JavaConfig;
import com.pms.repositories.PMSCompanyRepo;

/**
 * @author jifang
 *
 */

@ContextConfiguration(classes=JavaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class PMSCompanyControllerTest {
    

    @Autowired
    private TestEntityManager testEntityManager;
    
    @Autowired
    private PMSCompanyRepo companyRepo;
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.pms.controllers.PMSCompanyController#getCompanies()}.
     */
    @Test
    void testGetCompanies() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSCompanyController#createCompany(com.pms.entities.PMSCompany, org.springframework.validation.BindingResult)}.
     */
    @Test
    void testCreateCompany() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSCompanyController#deleteCompanies(java.util.List)}.
     */
    @Test
    void testDeleteCompanies() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSCompanyController#getCompany(java.lang.Long)}.
     */
    @Test
    void testGetCompany() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSCompanyController#updateCompany(java.lang.Long, com.pms.entities.PMSCompany, org.springframework.validation.BindingResult)}.
     */
    @Test
    void testUpdateCompany() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSCompanyController#deleteCompany(java.lang.Long)}.
     */
    @Test
    void testDeleteCompany() {
        fail("Not yet implemented");
    }

}
