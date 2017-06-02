/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.model;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 *
 * @author AnggaraNothing
 */
public class Relation extends LinkedHashMap<Symptom,Float> implements Serializable {
    
    private Disease disease;

    /**
     * Get the disease
     * @return the disease
     */
    public Disease getDisease() {
        return disease;
    }

    /**
     * Set the disease
     * @param disease the disease to set
     */
    public void setDisease(Disease disease) {
        this.disease = disease;
    }
}
