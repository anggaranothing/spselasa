/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.model;

import java.io.Serializable;

/**
 *
 * @author AnggaraNothing
 */
public class CfLevel implements Serializable {

    private float  value;
    private String description;

    /**
     * Get the certainty factor level value
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * Set the certainty factor level value
     * @param value the value to set
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Get the certainty factor level description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the certainty factor level description
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
