/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.client;

import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AnggaraNothing
 */
public class ContainerMap {
    private final Map< String, Container > map = new HashMap< String, Container >();
    
    /**
     * Get container object based on runtime classname
     * @param className the container runtime classname
     * @return the container object
     */
    public Container getContainer( String className ) {
        return map.get( className );
    }
    
    /**
     * Get map size.
     * @return The number of mapped container
     */
    public int counts() {
         return map.size();
    }
    
    /**
     * Return true if container exists on the map.
     * @param className the container runtime classname
     * @return True if container runtime classname exists on the map. Otherwise, return false
     */
    public boolean contains( String className ) {
        return map.containsKey( className );
    }
    
    /**
     * Return true if container exists on the map.
     * @param container the container object
     * @return True if container object exists on the map. Otherwise, return false
     */
    public boolean contains( Container container ) {
        return map.containsValue( container );
    }
    
    /**
     * Return true if container exists on the map.
     * @param className the container runtime classname
     * @param container the container object
     * @return True if runtime classname & container object exists on the map. Otherwise, return false
     */
    public boolean contains( String className , Container container ) {
        return contains( className ) && getContainer( className ) != null;
    }
    
    /**
     * Add container to the map. If the container already mapped before, show instead.
     * @param className the container class name
     * @param container the container object
     * @return added container
     */
    public Container add( String className, Container container ) {
        //System.out.println( className );
        if( contains( className , container ) ) {
            container.removeAll();
            container.removeNotify();
            
            container = getContainer( className );
        }
        else
            map.put( className, container );
        
        return container;
    }
    
    public Container add( Container container ) {
        return add( container.getClass().getName() , container );
    }
    
    /**
     * Add container to the map and show it. Replace old value if the container already mapped before.
     * @param className the container class name
     * @param container the container object
     * @return added container
     */
    public Container addAndShow( String className, Container container ) {
        container = add( className, container );
        
        container.setVisible( true );
        container.requestFocusInWindow();
        
        return container;
    }
    
    public Container addAndShow( Container container ) {
        return addAndShow( container.getClass().getName(), container );
    }
    
    /**
     * Remove container from the map.
     * @param className the container class name
     * @return removed container
     */
    public Container remove( String className ) {
        return map.remove( className );
    }
    
    /**
     * Clear all containers and their runtime classname.
     */
    public void clearAll() {
        map.clear();
    }
}
