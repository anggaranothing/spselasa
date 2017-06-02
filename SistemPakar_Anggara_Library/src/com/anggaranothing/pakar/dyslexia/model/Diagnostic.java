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
public class Diagnostic implements Serializable {
    public enum StatusEnum {
        done,cancel
    };
    
    private long id;
    private User user;
    private Timestamp date;
    private StatusEnum status;
    private String cfUser;
    private String cfCombined;
    private String cfPercentage;

    /**
     * Get diagnostic ID
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set diagnostic ID
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the user who run the diagnostic
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get diagnostic start date
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * Set diagnostic start date
     * @param date the date to set
     */
    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
     * Get diagnostic status
     * @return the status
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * Set diagnostic status
     * @param status the status to set
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * Get diagnostic CF User
     * @return the cfUser
     */
    public String getCfUser() {
        return cfUser;
    }

    /**
     * Set diagnostic user CF values
     * @param cfUser the cfUser to set
     */
    public void setCfUser(String cfUser) {
        this.cfUser = cfUser;
    }

    /**
     * Get diagnostic combined CF values
     * @return the cfCombined
     */
    public String getCfCombined() {
        return cfCombined;
    }

    /**
     * Set diagnostic combined CF values
     * @param cfCombined the cfCombined to set
     */
    public void setCfCombined(String cfCombined) {
        this.cfCombined = cfCombined;
    }

    /**
     * Get diagnostic result CF values
     * @return the cfPercentage
     */
    public String getCfPercentage() {
        return cfPercentage;
    }

    /**
     * Set diagnostic result CF values
     * @param cfPercentage the cfPercentage to set
     */
    public void setCfPercentage(String cfPercentage) {
        this.cfPercentage = cfPercentage;
    }
    
    
}
