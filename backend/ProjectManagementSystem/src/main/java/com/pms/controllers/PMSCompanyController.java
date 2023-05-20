/**
 * 
 */
package com.pms.controllers;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSCompany;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/companies", 
            produces="application/json", 
            consumes="application/json")
public class PMSCompanyController {
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @GetMapping(value="")
    public List<PMSCompany> getCompanies() {
    	return entityProvider.getCompanies();
    }
    
    @PostMapping(value="")
    @ResponseStatus(HttpStatus.CREATED)
    public PMSCompany createCompany(@RequestBody PMSCompany comp) {
        return entityProvider.createCompany(comp);
    }
    
    @GetMapping(value="/{id}")
    public List<PMSCompany> findCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return entityProvider.getCompaniesByIds(ids);
    }
    
    @PutMapping(value="/{id}")
    public PMSCompany updateCompany(@PathVariable("id") Long id, @RequestBody PMSCompany comp) {
        return entityProvider.updateCompany(id, comp);
    }
    
    @DeleteMapping(value="/{id}")
    public void deleteCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        entityProvider.deleteCompanies(ids);
    }

}
