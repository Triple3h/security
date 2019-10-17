package com.qf.security.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 81958
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping
    public String index(){
        return "index";
    }

}
