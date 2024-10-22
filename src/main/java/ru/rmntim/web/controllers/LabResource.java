package ru.rmntim.web.controllers;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import ru.rmntim.web.beans.UserBean;

@Path("/")
public class LabResource {
    @EJB
    private UserBean userBean;

    @GET
    @Produces("text/plain")
    public String hello() {
        return userBean.createUser();
    }
}