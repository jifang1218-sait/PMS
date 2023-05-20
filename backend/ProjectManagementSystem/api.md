# Project Management System Backend API list:  
### 1. company
- List all companies    
- URL: /companies  
- Method: GET  
- Headers:  
Content-Type:application/json  
Accept:application/json  

Example:  
```
curl -X GET -H "Content-Type:application/json" -H "Accept:application/json" http://localhost:8080/companies  

Return:  
List of companies in json format.(will add result later).  
--- 
- Create a company  
- URL: /companies  
- Method: POST  
- Headers:  
Content-Type:application/json, Accept:application/json  
  
Example:  
    curl -X POST -H "Content-Type:application/json" -H "Accept:application/json" -d '{companyJsonString}' http://localhost:8080/companies
dddddddd     

Return:  
Company object in json format.(will add result later). 
--- 

