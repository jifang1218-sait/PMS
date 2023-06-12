/**
 * 
 */
package com.pms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

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
import com.pms.entities.PMSCompany;


/**
 * @author jifang
 *
 */
@ContextConfiguration(classes=JavaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class PMSEntityProviderTest {
    @Autowired
    private TestEntityManager testEntityManager;
    
    @Autowired
    private PMSEntityProvider entityProvider;
    /**
     * @throws java.lang.Exception
     */
    static void setUpBeforeClass() throws Exception {
    }
    
    @BeforeAll
    static void setUpBeforeClass1() throws Exception {
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
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getCompanies()}.
     */
    @Test
    void testGetCompanies() {
    	// create 1 company then check it then delete it. 
        PMSCompany company = new PMSCompany();
        company.setAvatar("company0avatar");
        company.setDesc("company0desc");
        company.setName("company0name");
        company = testEntityManager.persistAndFlush(company);
        
        List<PMSCompany> companies = entityProvider.getCompanies();
        assertEquals(companies.size(), 1);
        PMSCompany created = companies.get(0);
        assertEquals(company.getAvatar(), created.getAvatar());
        assertEquals(company.getDesc(), created.getDesc());
        assertEquals(company.getName(), created.getName());
        
        testEntityManager.remove(company);
        company = testEntityManager.find(PMSCompany.class, company.getId());
        assertNull(company);
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#createCompany(com.pms.entities.PMSCompany)}.
     */
    @Test
    void testCreateCompany() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getCompaniesByIds(java.util.List)}.
     */
    @Test
    void testGetCompaniesByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#updateCompany(java.lang.Long, com.pms.entities.PMSCompany)}.
     */
    @Test
    void testUpdateCompany() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#cleanupCompanies(java.util.List)}.
     */
    @Test
    void testCleanupCompanies() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getProjects()}.
     */
    @Test
    void testGetProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getProjectsByCompanyId(java.lang.Long)}.
     */
    @Test
    void testGetProjectsByCompanyId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getProjectsByIds(java.util.List)}.
     */
    @Test
    void testGetProjectsByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#createProject(java.lang.Long, com.pms.entities.PMSProject)}.
     */
    @Test
    void testCreateProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#updateProject(java.lang.Long, com.pms.entities.PMSProject)}.
     */
    @Test
    void testUpdateProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#cleanupProjects(java.util.List)}.
     */
    @Test
    void testCleanupProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#addDependentProjects(long, java.util.List)}.
     */
    @Test
    void testAddDependentProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#setDependentProjects(long, java.util.List)}.
     */
    @Test
    void testSetDependentProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#removeDependentProjects(long, java.util.List)}.
     */
    @Test
    void testRemoveDependentProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getDependentProjectsById(long)}.
     */
    @Test
    void testGetDependentProjectsById() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#isUserExistsInProject(long, long)}.
     */
    @Test
    void testIsUserExistsInProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#isUserExistsInTask(long, long)}.
     */
    @Test
    void testIsUserExistsInTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getTasks()}.
     */
    @Test
    void testGetTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getTasksByProjectId(java.lang.Long)}.
     */
    @Test
    void testGetTasksByProjectId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getTasksByIds(java.util.List)}.
     */
    @Test
    void testGetTasksByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#createTask(java.lang.Long, com.pms.entities.PMSTask)}.
     */
    @Test
    void testCreateTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#updateTask(java.lang.Long, com.pms.entities.PMSTask)}.
     */
    @Test
    void testUpdateTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#cleanupTasks(java.util.List)}.
     */
    @Test
    void testCleanupTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#addDependentTasks(long, java.util.List)}.
     */
    @Test
    void testAddDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#setDependentTasks(long, java.util.List)}.
     */
    @Test
    void testSetDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#removeDependentTasks(long, java.util.List)}.
     */
    @Test
    void testRemoveDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getDependentTasks(long)}.
     */
    @Test
    void testGetDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#createCommentForProject(java.lang.Long, com.pms.entities.PMSComment)}.
     */
    @Test
    void testCreateCommentForProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#createCommentForTask(java.lang.Long, com.pms.entities.PMSComment)}.
     */
    @Test
    void testCreateCommentForTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#cleanupComments(java.util.List)}.
     */
    @Test
    void testCleanupComments() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getCommentsByTask(long)}.
     */
    @Test
    void testGetCommentsByTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getComments(java.util.List)}.
     */
    @Test
    void testGetComments() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getCommentsForProjectOnly(long)}.
     */
    @Test
    void testGetCommentsForProjectOnly() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getCommentsByProject(long)}.
     */
    @Test
    void testGetCommentsByProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#updateComment(java.lang.Long, com.pms.entities.PMSComment)}.
     */
    @Test
    void testUpdateComment() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#createUser(com.pms.entities.PMSUser)}.
     */
    @Test
    void testCreateUser() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getUsersByProject(long)}.
     */
    @Test
    void testGetUsersByProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getUsersByTaskId(long)}.
     */
    @Test
    void testGetUsersByTaskId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getUsers()}.
     */
    @Test
    void testGetUsers() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getUsersByIds(java.util.List)}.
     */
    @Test
    void testGetUsersByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#addUsersToTask(long, java.util.List)}.
     */
    @Test
    void testAddUsersToTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#addUsersToProject(long, java.util.List)}.
     */
    @Test
    void testAddUsersToProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#setUsersToTask(long, java.util.List)}.
     */
    @Test
    void testSetUsersToTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#setUsersToProject(long, java.util.List)}.
     */
    @Test
    void testSetUsersToProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#removeUsersFromTask(long, java.util.List)}.
     */
    @Test
    void testRemoveUsersFromTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#removeUsersFromProject(long, java.util.List)}.
     */
    @Test
    void testRemoveUsersFromProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#updateUser(java.lang.Long, com.pms.entities.PMSUser)}.
     */
    @Test
    void testUpdateUser() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#deleteUsers(java.util.List)}.
     */
    @Test
    void testDeleteUsers() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getProjectsByUserId(java.lang.Long)}.
     */
    @Test
    void testGetProjectsByUserId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getTasksByUserId(java.lang.Long)}.
     */
    @Test
    void testGetTasksByUserIdLong() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.controllers.PMSEntityProvider#getTasksByUserId(java.lang.Long, java.lang.Long)}.
     */
    @Test
    void testGetTasksByUserIdLongLong() {
        fail("Not yet implemented");
    }

}
