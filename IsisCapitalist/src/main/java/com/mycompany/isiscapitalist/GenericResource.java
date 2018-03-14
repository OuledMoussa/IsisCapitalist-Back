/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.isiscapitalist;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import com.mycompany.isiscapitalist.Services;

/**
 * REST Web Service
 *
 * @author houledmo
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;
    static Services services;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
        services = new Services();
        services.readWorldFromXml();
    }

    /**
     * Retrieves representation of an instance of com.mycompany.isiscapitalist.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("world")
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        services.readWorldFromXml();
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
