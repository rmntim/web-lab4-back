package ru.rmntim.web.controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/")
public class LabResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}