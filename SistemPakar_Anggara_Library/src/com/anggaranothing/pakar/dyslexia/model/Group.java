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
public class Group implements Serializable {

    private int id;
    private String name;

    /**
     * Get group ID.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Set group ID.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get group name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set group name.
     * @param nama the name to set
     */
    public void setName(String nama) {
        this.name = nama;
    }
    
}
