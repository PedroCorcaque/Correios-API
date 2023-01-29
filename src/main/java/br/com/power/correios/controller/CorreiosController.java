package br.com.power.correios.controller;

import br.com.power.correios.exception.NoContentException;
import br.com.power.correios.exception.NotReadyException;
import br.com.power.correios.model.Address;
import br.com.power.correios.service.CorreiosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class CorreiosController {

    @Autowired
    private CorreiosService service;

    @GetMapping("/status")
    public String getStatus() {
        return "Service Status: " + service.getStatus();
    }

    @GetMapping("/zipcode/{zipcode}")
    public Address getAddressByZipcode(@PathVariable("zipcode") String zipcode) throws NoContentException, NotReadyException {
        return this.service.getAddressByZipcode(zipcode);
    }
}
