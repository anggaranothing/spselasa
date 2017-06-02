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
public class User implements Serializable {

    private long id;
    private String email;
    private String name;
    private String pass;
    private boolean female;
    private int grup;
    private Timestamp register;
    private Timestamp lastlogin;

    /**
     * Get User ID.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set user ID.
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get user email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user email.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get user name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set user name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get user password.
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Set user password.
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Get user gender.
     * True if user is female.
     * @return the female
     */
    public boolean isFemale() {
        return female;
    }

    /**
     * Set user gender.
     * True if user is female.
     * @param female the female to set
     */
    public void setFemale(boolean female) {
        this.female = female;
    }

    /**
     * @return the grup
     */
    public int getGrup() {
        return grup;
    }

    /**
     * @param grup the grup to set
     */
    public void setGrup(int grup) {
        this.grup = grup;
    }

    /**
     * @return the register
     */
    public Timestamp getRegister() {
        return register;
    }

    /**
     * @param register the register to set
     */
    public void setRegister(Timestamp register) {
        this.register = register;
    }

    /**
     * @return the lastlogin
     */
    public Timestamp getLastlogin() {
        return lastlogin;
    }

    /**
     * @param lastlogin the lastlogin to set
     */
    public void setLastlogin(Timestamp lastlogin) {
        this.lastlogin = lastlogin;
    }
}
