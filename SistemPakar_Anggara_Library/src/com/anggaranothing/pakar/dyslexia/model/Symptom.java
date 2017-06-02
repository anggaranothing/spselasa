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
public class Symptom implements Serializable {
    private String id;
    private String description;

    /**
     * Get symptom ID
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Set symptom ID
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get symptom description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set symptom description
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
