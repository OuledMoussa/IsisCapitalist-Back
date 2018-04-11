/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.isiscapitalist;

import com.google.gson.Gson;
import generated.PallierType;
import generated.ProductType;
import generated.World;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

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
    }
    
   /////////////////// Version SANS utilisateur ///////////////////
  
    /*@GET
    @Path("world")
    @Produces(MediaType.APPLICATION_XML)
    public World getXml() throws JAXBException {
        World world = services.readWorldFromXml();
        services.saveWorldToXml(world);
            return(world);
    }
    
    
    @GET
    @Path("world2")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() throws JAXBException { 
        World world = services.readWorldFromXml();
        return(new Gson().toJson(world));
    }
    */
    /////////////////////////////////////////////////////////

    
    /////////////////// Version AVEC utilisateur ///////////////////

    @GET
    @Path("world")
    @Produces(MediaType.APPLICATION_XML)
    public World getXml(@Context HttpServletRequest request) throws JAXBException {
        String username = request.getHeader("X-user");
        World world = services.readWorldFromXml(username);
        services.saveWorldToXml(world, username);
        return(world);
    }
    
    @GET
    @Path("world2")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@Context HttpServletRequest request) throws JAXBException { 
        String username = request.getHeader("X-user");
        World world = services.readWorldFromXml(username);
        return(new Gson().toJson(world));
    }
    
    @PUT
    @Path("product")
    @Consumes(MediaType.APPLICATION_JSON)
    public void product (@Context HttpServletRequest request, String content) throws JAXBException { 
        String username = request.getHeader("X-user");
        ProductType product = new Gson().fromJson(content, ProductType.class);
        //
    }
    
    @PUT
    @Path("manager")
    @Consumes(MediaType.APPLICATION_JSON)
    public void manager (@Context HttpServletRequest request, String content) throws JAXBException { 
        String username = request.getHeader("X-user");
        PallierType pallier = new Gson().fromJson(content, PallierType.class);
        //
    }
}
