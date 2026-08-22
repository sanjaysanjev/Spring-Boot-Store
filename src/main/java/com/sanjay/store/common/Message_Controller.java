package com.sanjay.store.common;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Message_Controller {

    @RequestMapping("/hello")
    public Message hello()
    {
        return new Message("Hello World");

    }
}
