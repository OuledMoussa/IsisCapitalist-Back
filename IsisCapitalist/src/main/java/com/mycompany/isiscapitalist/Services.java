package com.mycompany.isiscapitalist;


import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import generated.World;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.Marshaller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author houledmo
 */
public class Services {
    public Services(){
        
    }
    
    public World readWorldFromXml(){
        World world=null;
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Unmarshaller u = cont.createUnmarshaller();
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            world = (World) u.unmarshal(input);
            System.out.println(world);
        }
            catch(Exception e){
                System.out.println(e.getMessage());
            }  
        return world;
    }
    
    private void saveWorldToXml(World world){
        try{
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            OutputStream output = new FileOutputStream("world.xml");
            m.marshal(world, output);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
}
