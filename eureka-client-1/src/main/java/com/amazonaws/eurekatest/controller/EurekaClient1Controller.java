package com.amazonaws.eurekatest.controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;;

@RestController
public class EurekaClient1Controller
{
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    
    private RestTemplate restTemplate = new  RestTemplate() ;

    @GetMapping("/callEurekaClient1")
    public ResponseEntity<String> callEurekaClientOne() throws Exception {

        String clientResponse = "Eureka Demo - Response from Eureka Client 1";
        return new ResponseEntity<String>(clientResponse,HttpStatus.OK);
    }

    @GetMapping("/callEurekaClient2FromClient1")
        public ResponseEntity<String> callEurekaClientOneFromTwo() throws Exception {

       try{
           return new ResponseEntity<String>(restTemplate.getForObject(getBaseUrl()+"/callEurekaClient2", String.class), HttpStatus.OK);

       }catch(Exception e)
       {
            return new ResponseEntity<String>(restTemplate.getForObject(getBaseUrl()+"/callEurekaClient2", String.class), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    private String getBaseUrl(){
        ServiceInstance instance = loadBalancerClient.choose("EUREKA-CLIENT-2");
        System.out.println("Mayur getbaseurl ==>"+instance.getUri());
        System.out.println("Mayur getbaseurl string==>"+instance.getUri().toString());
        return instance.getUri().toString();

    }

}