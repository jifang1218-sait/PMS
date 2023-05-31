/**
 * 
 */
package com.pms.controllers;

import java.util.List;
import javax.validation.Valid;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSCompany;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/v1/old/entities", 
            produces="application/json", 
            consumes="application/json")
@Transactional
public class PMSCompanyController {
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    private final static Logger logger =
            LoggerFactory.getLogger(PMSCompanyController.class);
    
    /**
     * the uniform url
     */
    @GetMapping(value="/companies")
    public List<PMSCompany> getCompanies() {
        return entityProvider.getCompanies();
    }
    
    @PostMapping(value="/companies")
    public ResponseEntity<PMSCompany> createCompany(@RequestBody @Valid PMSCompany comp, BindingResult result) {
        if(result.hasErrors()) {
            return new ResponseEntity<>(comp, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.createCompany(comp), HttpStatus.CREATED);
    } 
    
    @DeleteMapping(value="/companies")
    public void deleteCompanies(@RequestBody List<Long> companyIds) {
        entityProvider.cleanupCompanies(companyIds);
    }
    
    @GetMapping(value="/companies/{id}")
    public PMSCompany getCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return entityProvider.getCompaniesByIds(ids).get(0);
    }
    
    @PutMapping(value="/companies/{id}")
    public ResponseEntity<PMSCompany> updateCompany(@PathVariable("id") Long id, 
            @RequestBody @Valid PMSCompany comp, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(comp, HttpStatus.BAD_REQUEST);
        } 
        
        return new ResponseEntity<>(entityProvider.updateCompany(id, comp), HttpStatus.OK);
    }
    
    @DeleteMapping(value="/companies/{id}")
    public void deleteCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        entityProvider.cleanupCompanies(ids);
    }
    
}
