//
//package com.example.spring_integration_prototype.web;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class UppercaseController {
//
//    private final UppercaseGateway uppercaseGateway;
//
//    @Autowired
//    public UppercaseController(UppercaseGateway uppercaseGateway) {
//        this.uppercaseGateway = uppercaseGateway;
//    }
//
//    @PostMapping("/uppercase")
//    public void uppercase(@RequestBody String text) {
//        uppercaseGateway.uppercase(text);
//    }
//}
