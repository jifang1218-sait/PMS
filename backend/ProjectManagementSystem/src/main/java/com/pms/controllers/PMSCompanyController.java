/**
 * 
 */
package com.pms.controllers;

import java.util.List;
import javax.validation.Valid;
import java.util.ArrayList;

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
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSCompany;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/v1/entities/companies", 
            produces="application/json", 
            consumes="application/json")
@Transactional
public class PMSCompanyController {
    
    @Autowired
    private PMSEntityProvider entityProvider;
    
    @GetMapping
    public List<PMSCompany> getCompanies() {
    	return entityProvider.getCompanies();
    }
    
    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PMSCompany> createCompany(@RequestBody @Valid PMSCompany comp, BindingResult result) {
        if(result.hasErrors()) {
            return new ResponseEntity<>(comp, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(entityProvider.createCompany(comp), HttpStatus.CREATED);
    }
    
    @GetMapping(value="/{id}")
    public List<PMSCompany> findCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return entityProvider.getCompaniesByIds(ids);
    }
    
    @PutMapping(value="/{id}")
    public ResponseEntity<PMSCompany> updateCompany(@PathVariable("id") Long id, 
            @RequestBody @Valid PMSCompany comp, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(comp, HttpStatus.BAD_REQUEST);
        } 
        
        return new ResponseEntity<>(entityProvider.updateCompany(id, comp), HttpStatus.OK);
    }
    
    @DeleteMapping(value="/{id}")
    public void deleteCompany(@PathVariable("id") Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        entityProvider.deleteCompanies(ids);
    }

}
