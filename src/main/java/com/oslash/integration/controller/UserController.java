package com.oslash.integration.controller;

import com.oslash.integration.plugin.GDrivePlugin;
import com.oslash.integration.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("drive")
public class UserController {

    @Autowired
    private IntegrationService integrationService;

    @PostMapping("integrate")
    public void integrate() {
        integrationService.integrate(
                new GDrivePlugin()
        );
    }

}