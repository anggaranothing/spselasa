package com.anggaranothing.pakar.dyslexia;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template fileObj, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import org.ini4j.Wini;

/**
 *
 * @author AnggaraNothing
 */
public abstract class Config {    
    protected static File                 fileObj;
    protected static Wini                 iniObj;

    /**
     * Get the (W)INI object.
     * @return the iniObj
     */
    public static Wini getIniObj() {
        return iniObj;
    }
    
    /**
     * Get the (W)INI key value on section.
     * @param sectionName Section name
     * @param optionName  Key name
     * @param clazz Class type ( int.class, etc. )
     * @return the value
     */
    public static <T extends Object> T read( Object sectionName, Object optionName, Class<T> clazz ) {
        return getIniObj().get( sectionName, optionName, clazz );
    }
    
    /**
     * Get the (W)INI key value on section.
     * @param sectionName Section name
     * @param optionName  Key name
     * @return the String value
     */
    public static String read( Object sectionName, Object optionName ) {
        return getIniObj().get( sectionName, optionName );
    }
    
    /**
     * Get the (W)INI key value on section using fetch. Designed for macros.
     * @param sectionName Section name
     * @param optionName  Key name
     * @param clazz Class type ( int.class, etc. )
     * @return the value
     */
    public static <T extends Object> T parse( Object sectionName, Object optionName, Class<T> clazz ) {
        return getIniObj().fetch( sectionName, optionName, clazz );
    }
    
    /**
     * Get the (W)INI key value on section using fetch. Designed for macros.
     * @param sectionName Section name
     * @param optionName  Key name
     * @return the String value
     */
    public static String parse( Object sectionName, Object optionName ) {
        return getIniObj().fetch( sectionName, optionName );
    }
    
    /**
     * Add/Set the value to the INI and store.
     * @param sectionName Section name
     * @param keyName  Key name
     * @param value Value to set
     * @throws java.io.IOException
     */
    public static void update( String sectionName, String keyName, Object value ) throws IOException {
        if( getIniObj().put( sectionName , keyName , value ) == null ) {
            getIniObj().add( sectionName , keyName , value );
        }
        getIniObj().store();
    }
    
    /**
     * Clear the key value. Set to empty ( "" )
     * @param sectionName Section name
     * @param keyName  Key name
     * @throws java.io.IOException
     */
    public static void clear( String sectionName, String keyName ) throws IOException {
        update( sectionName, keyName, "" );
    }
}
