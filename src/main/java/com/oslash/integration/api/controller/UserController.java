package com.oslash.integration.api.controller;

import com.oslash.integration.api.exception.IntegrationException;
import com.oslash.integration.api.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("integrate")
public class UserController {

    @Autowired
    private IntegrationService integrationService;

    @GetMapping("drive")
    public void integrate() throws IntegrationException {

        integrationService.integrateDrive();
    }

}