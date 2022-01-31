package com.amazonaws.eurekatest.controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
@RestController
public class EurekaClient2Controller
{

    @GetMapping("/callEurekaClient2")
    public ResponseEntity<String> callEurekaClientTwo() throws Exception {

        String clientResponse = "Eureka Demo - Response from Eureka Client 2";
        return new ResponseEntity<String>(clientResponse,HttpStatus.OK);
    }
}