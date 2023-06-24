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
import com.pms.entities.PMSFile;
import com.pms.services.PMSEntityProvider;


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
     * Test method for {@link com.pms.services.PMSEntityProvider#getCompanies()}.
     */
    @Test
    void testGetCompanies() {
    	// create 1 company then check it then delete it. 
        PMSCompany company = new PMSCompany();
        PMSFile avatar = new PMSFile();
		avatar.setRealFilename("company0avatar");
        company.setAvatar(avatar);
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
     * Test method for {@link com.pms.services.PMSEntityProvider#createCompany(com.pms.entities.PMSCompany)}.
     */
    @Test
    void testCreateCompany() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getCompaniesByIds(java.util.List)}.
     */
    @Test
    void testGetCompaniesByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#updateCompany(java.lang.Long, com.pms.entities.PMSCompany)}.
     */
    @Test
    void testUpdateCompany() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#cleanupCompanies(java.util.List)}.
     */
    @Test
    void testCleanupCompanies() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getProjects()}.
     */
    @Test
    void testGetProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getProjectsByCompanyId(java.lang.Long)}.
     */
    @Test
    void testGetProjectsByCompanyId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getProjectsByIds(java.util.List)}.
     */
    @Test
    void testGetProjectsByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#createProject(java.lang.Long, com.pms.entities.PMSProject)}.
     */
    @Test
    void testCreateProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#updateProject(java.lang.Long, com.pms.entities.PMSProject)}.
     */
    @Test
    void testUpdateProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#cleanupProjects(java.util.List)}.
     */
    @Test
    void testCleanupProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#addDependentProjects(long, java.util.List)}.
     */
    @Test
    void testAddDependentProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#setDependentProjects(long, java.util.List)}.
     */
    @Test
    void testSetDependentProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#removeDependentProjects(long, java.util.List)}.
     */
    @Test
    void testRemoveDependentProjects() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getDependentProjectsById(long)}.
     */
    @Test
    void testGetDependentProjectsById() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#isUserExistsInProject(long, long)}.
     */
    @Test
    void testIsUserExistsInProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#isUserExistsInTask(long, long)}.
     */
    @Test
    void testIsUserExistsInTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getTasks()}.
     */
    @Test
    void testGetTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getTasksByProjectId(java.lang.Long)}.
     */
    @Test
    void testGetTasksByProjectId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getTasksByIds(java.util.List)}.
     */
    @Test
    void testGetTasksByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#createTask(java.lang.Long, com.pms.entities.PMSTask)}.
     */
    @Test
    void testCreateTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#updateTask(java.lang.Long, com.pms.entities.PMSTask)}.
     */
    @Test
    void testUpdateTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#cleanupTasks(java.util.List)}.
     */
    @Test
    void testCleanupTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#addDependentTasks(long, java.util.List)}.
     */
    @Test
    void testAddDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#setDependentTasks(long, java.util.List)}.
     */
    @Test
    void testSetDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#removeDependentTasks(long, java.util.List)}.
     */
    @Test
    void testRemoveDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getDependentTasks(long)}.
     */
    @Test
    void testGetDependentTasks() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#createCommentForProject(java.lang.Long, com.pms.entities.PMSComment)}.
     */
    @Test
    void testCreateCommentForProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#createCommentForTask(java.lang.Long, com.pms.entities.PMSComment)}.
     */
    @Test
    void testCreateCommentForTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#cleanupComments(java.util.List)}.
     */
    @Test
    void testCleanupComments() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getCommentsByTask(long)}.
     */
    @Test
    void testGetCommentsByTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getComments(java.util.List)}.
     */
    @Test
    void testGetComments() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getCommentsForProjectOnly(long)}.
     */
    @Test
    void testGetCommentsForProjectOnly() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getCommentsByProject(long)}.
     */
    @Test
    void testGetCommentsByProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#updateComment(java.lang.Long, com.pms.entities.PMSComment)}.
     */
    @Test
    void testUpdateComment() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#createUser(com.pms.entities.PMSUser)}.
     */
    @Test
    void testCreateUser() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getUsersByProject(long)}.
     */
    @Test
    void testGetUsersByProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getUsersByTaskId(long)}.
     */
    @Test
    void testGetUsersByTaskId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getUsers()}.
     */
    @Test
    void testGetUsers() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getUsersByIds(java.util.List)}.
     */
    @Test
    void testGetUsersByIds() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#addUsersToTask(long, java.util.List)}.
     */
    @Test
    void testAddUsersToTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#addUsersToProject(long, java.util.List)}.
     */
    @Test
    void testAddUsersToProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#setUsersToTask(long, java.util.List)}.
     */
    @Test
    void testSetUsersToTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#setUsersToProject(long, java.util.List)}.
     */
    @Test
    void testSetUsersToProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#removeUsersFromTask(long, java.util.List)}.
     */
    @Test
    void testRemoveUsersFromTask() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#removeUsersFromProject(long, java.util.List)}.
     */
    @Test
    void testRemoveUsersFromProject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#updateUser(java.lang.Long, com.pms.entities.PMSUser)}.
     */
    @Test
    void testUpdateUser() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#deleteUsers(java.util.List)}.
     */
    @Test
    void testDeleteUsers() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getProjectsByUserId(java.lang.Long)}.
     */
    @Test
    void testGetProjectsByUserId() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getTasksByUserId(java.lang.Long)}.
     */
    @Test
    void testGetTasksByUserIdLong() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.pms.services.PMSEntityProvider#getTasksByUserId(java.lang.Long, java.lang.Long)}.
     */
    @Test
    void testGetTasksByUserIdLongLong() {
        fail("Not yet implemented");
    }

}
