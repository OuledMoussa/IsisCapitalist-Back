package com.mycompany.isiscapitalist;

import static com.mycompany.isiscapitalist.GenericResource.services;
import generated.PallierType;
import generated.ProductType;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import generated.World;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author achaumei
 */
public class Services {
    
    /////////////////// Version SANS utilisateur ///////////////////

    /*public World readWorldFromXml() throws JAXBException {
        JAXBContext cont = JAXBContext.newInstance(World.class);
        Unmarshaller u = cont.createUnmarshaller();
        World world;
        try {
            world = (World) u.unmarshal(new File("world.xml"));
        } catch (JAXBException e) {
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            world = (World) u.unmarshal(input);
            System.out.println(e.getMessage());
        }
        return world;
    }
     
    public void saveWorldToXml(World world) {
        try {
            OutputStream output = new FileOutputStream("world.xml");
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            m.marshal(world, output);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
     
    public World getWorld() throws JAXBException {
        World world = readWorldFromXml();
        saveWorldToXml(world);
        return world;
    }*/
    
    /////////////////////////////////////////////////////////
    
    /////////////////// Version SANS utilisateur ///////////////////
    
    // lit le monde a partir d'un fichier XML
    public World readWorldFromXml(String username) throws JAXBException {
        JAXBContext cont = JAXBContext.newInstance(World.class);
        Unmarshaller u = cont.createUnmarshaller();
        World world;
        try {
            world = (World) u.unmarshal(new File(username + "-world.xml"));
        } catch (JAXBException e) {
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            world = (World) u.unmarshal(input);
            System.out.println(e.getMessage());
        }
        //UpdateScore (world);
        return world;
    }
    
    // sauvegarde le monde dans un fichier XML
    public void saveWorldToXml(World world, String username) {
        try {
            OutputStream output = new FileOutputStream(username + "-world.xml");
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            m.marshal(world, output);
            //UpdateScore (world);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public World getWorld(String username) throws JAXBException {
        World world = readWorldFromXml(username);
        saveWorldToXml(world, username);
        //UpdateScore (world);
        return world;
    }
    
    //Trouver un produit a partir de son ID
    public ProductType findProductById(World world, int productId){
        List<ProductType> products = world.getProducts().getProduct();
        for (ProductType product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }
    
    //Trouver un manager a partir de son name
    public PallierType findManagerByName(World world, String managerName){
        List<PallierType> managers = world.getManagers().getPallier();
        for (PallierType manager : managers) {
            if (manager.getName() == managerName) {
                return manager;
            }
        }
        return null;
    }
    
    // prend en paramètre le pseudo du joueur et le produit 
    // sur lequel une action a eu lieu (lancement manuel de production ou 
    // achat d’une certaine quantité de produit) 
    // renvoie false si l’action n’a pas pu être traitée 
    public Boolean updateProduct(String username, ProductType newproduct) throws JAXBException { 
        World world = getWorld(username); // aller chercher le monde qui correspond au joueur 
        ProductType product = findProductById(world, newproduct.getId());         // trouver dans ce monde, le produit équivalent à celui passé en paramètre 
        if (product == null) { return false;} 
            // calculer la variation de quantité. Si elle est positive c'est 
            // que le joueur a acheté une certaine quantité de ce produit 
            int qtchange = newproduct.getQuantite() - product.getQuantite(); 
            if (qtchange > 0) { 
                world.setMoney(world.getMoney() - (product.getCout() * ((1 - Math.pow(product.getCroissance(), qtchange)) / (1 - product.getCroissance())))); // soustraire de l'argent du joueur le cout de la quantité achete
                product.setQuantite(product.getQuantite() + qtchange); // mettre à jour la quantité de product 
            } else { // sinon c’est qu’il s’agit d’un lancement de production. 
                product.setTimeleft(newproduct.getVitesse());// initialiser product.timeleft à product.vitesse pour lancer la production 
            } 
                // sauvegarder les changements du monde 
                saveWorldToXml(world, username); 
                return true; 
            }
    
    // prend en paramètre le pseudo du joueur et le manager acheté. 
    // renvoie false si l’action n’a pas pu être traitée 
    public Boolean updateManager(String username, PallierType newmanager) throws JAXBException { 
        World world = getWorld(username); // aller chercher le monde qui correspond au joueur 
        PallierType manager = findManagerByName(world, newmanager.getName()); // trouver dans ce monde, le manager équivalent à celui passé en paramètre 
        if (manager == null) { 
            return false; 
        }
        manager.setUnlocked(true);// débloquer ce manager 
        ProductType product = findProductById(world, manager.getIdcible());  // trouver le produit correspondant au manager 
        if (product == null) { 
            return false;
        } 
        product.setManagerUnlocked(true);// débloquer le manager de ce produit 
        world.setMoney(world.getMoney() - manager.getSeuil()); // soustraire de l'argent du joueur le cout du manager 
        saveWorldToXml(world, username); // sauvegarder les changements au monde 
        //UpdateScore (world);
        return true; 
    }
    
    public void UpadateScore(World world){
        long timeEcoul = System.currentTimeMillis() - world.getLastupdate(); //calcul du temps écoule
        List<ProductType> products = world.getProducts().getProduct();
        for (ProductType product : products) { //Parcourir chaque produits
            //calculer combien d'exemplaire du produits ont été crée depuis la dernière fois,
            if (product.isManagerUnlocked()){  // si produit manager true
                 // vérifier timeleft!=null && timeleft <System.currentTimeMillis()
           
            } else {  // si produit manager false
            
            }
               
        }
            
       
    }
    /////////////////////////////////////////////////////////
}