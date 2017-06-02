/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author AnggaraNothing
 */
public class RelationOBSOLETE implements Serializable {
    
    private final Map<Disease,Set<Symptom>> relationMap = new HashMap<Disease,Set<Symptom>>();

    /**
     * Get the relation Map object
     * @return the relationMap
     */
    /*public Map<Disease,Set<Symptom>> getRelationMap() {
        return relationMap;
    }*/
    
    public int size() {
        return relationMap.size();
    }
    
    public Set<Map.Entry<Disease,Set<Symptom>>> entrySet() {
        return relationMap.entrySet();
    }
    
    /**
     * Add both disease and symptom to the map
     * @param disease the disease object as key
     * @param symptom the symptom object as value
     */
    public void addBoth( Disease disease , Symptom symptom ) {
        addDisease( disease );
        addSymptom( disease , symptom );
    }
    
    /**
     * Add both disease and set of symptoms to the map
     * @param disease the disease object as key
     * @param symptomSet the set of symptoms as value
     */
    public void addBoth( Disease disease , Set<Symptom> symptomSet ) {
        relationMap.putIfAbsent( disease, symptomSet );
    }
    
    /**
     * Add both disease and collection of symptoms to the map
     * @param disease the disease object as key
     * @param smpCollect the collection of symptoms as value
     */
    public void addBoth( Disease disease , Collection<Symptom> smpCollect ) {
        addDisease( disease );
        addAllSymptoms( disease, smpCollect );
    }
    
    /**
     * Add new disease to the map
     * @param disease the disease object as key
     */
    public void addDisease( Disease disease ) {
        addBoth( disease, new HashSet<Symptom>() );
    }
    
    /**
     * Get disease from a set of symptoms
     * @param symptomSet Set of symptoms
     * @return Disease object
     */
    public Disease getDisease( Set<Symptom> symptomSet ) {
        if( symptomSet == null )
            return null;
        
        for ( Map.Entry<Disease,Set<Symptom>> entry : relationMap.entrySet() )
        {
            if( entry.getValue() != null && entry.getValue().equals( symptomSet ) )
                return entry.getKey();
        }
        return null;
    }
    
    /**
     * Get disease from first occurrence of specified symptom
     * @param symptom Symptom object to find
     * @return Disease object
     */
    public Disease getDisease( Symptom symptom ) {
        if( symptom == null )
            return null;
        
        for ( Map.Entry<Disease,Set<Symptom>> entry : relationMap.entrySet() )
        {
            for( Symptom smp : entry.getValue() ) {
                if( smp != null && smp.getId().equalsIgnoreCase( symptom.getId() ) )
                    return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Replace a disease with a new set of symptoms
     * @param disease the disease object as key
     * @param symptomSet Set of symptoms
     */
    public void updateDisease( Disease disease, Set<Symptom> symptomSet ) {
        relationMap.replace( disease, symptomSet );
    }
    
    /**
     * Remove a disease from the map
     * @param disease the disease object as key
     */
    public void removeDisease( Disease disease ) {
        relationMap.remove( disease );
    }
    
    /**
     * Remove all diseases and their symptoms from the map
     * @param disease the disease object as key
     */
    public void removeAllDiseases( ) {
        relationMap.clear();
    }
    
    /**
     * Add a symptom to a disease
     * @param disease the disease to add
     * @param symptom the symptom object
     * @return
     */
    public boolean addSymptom( Disease disease , Symptom symptom ) {
        return getAllSymptoms( disease ).add( symptom );
    }
    
    /**
     * Add all symptoms to a disease
     * @param disease the disease to add
     * @param smpCollect Collection of symptoms
     * @return True if succeed
     */
    public boolean addAllSymptoms( Disease disease , Collection<Symptom> smpCollect ) {
        return getAllSymptoms( disease ).addAll( smpCollect );
    }
    
    /**
     * Get all symptoms from a disease
     * @param disease the disease object
     * @return Set of symptoms
     */
    public Set<Symptom> getAllSymptoms( Disease disease ) {
        return relationMap.get( disease );
    }
    
    /**
     * Remove a symptom from a disease
     * @param disease the disease object
     * @param symptomp the symptom object
     * @return True if succeed
     */
    public boolean removeSymptom( Disease disease , Symptom symptomp ) {
        return getAllSymptoms( disease ).remove( symptomp );
    }
    
    /**
     * Remove all symptoms from a disease
     * @param disease the disease object
     */
    public void removeAllSymptoms( Disease disease ) {
        getAllSymptoms( disease ).clear();
    }
}
