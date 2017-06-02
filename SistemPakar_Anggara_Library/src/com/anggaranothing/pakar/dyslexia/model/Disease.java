/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author AnggaraNothing
 */
public class Disease implements Serializable {
    private String id;
    private String name;
    private String description;
    private String prevention;
    private String treatment;
    private String picture;
    private Timestamp lastUpdate;

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
     * Get symptom name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set symptom name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * Get symptom prevention
     * @return the prevention
     */
    public String getPrevention() {
        return prevention;
    }

    /**
     * Set symptom prevention
     * @param prevention the prevention to set
     */
    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }

    /**
     * Get symptom treatment
     * @return the treatment
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Set symptom treatment
     * @param treatment the treatment to set
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     * Get symptom picture
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Set symptom picture
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Get symptom last update timestamp
     * @return the lastUpdate
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Set symptom last update timestamp
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
