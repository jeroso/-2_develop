package com.github.prgrms.social.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class HealthCheckRestController {

    @GetMapping(path = "_hcheck")
    public Long HealthCheck(){
        return System.currentTimeMillis();
    }
}
