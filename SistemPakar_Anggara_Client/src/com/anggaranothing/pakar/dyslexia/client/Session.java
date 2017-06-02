/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.client;

import com.anggaranothing.pakar.dyslexia.model.User;

/**
 *
 * @author AnggaraNothing
 */
public class Session {
    private User user = null;
    
    /**
     * True if user object is not null
     * @return True if user object is not null. Otherwise false.
     */
    public boolean isUserLogged() {
        return getUser() != null;
    }

    /**
     * Get the user object
     * @return the user object
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user object
     * @param user user object to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Remove the user object
     */
    public void removeUser() {
        setUser( null );
    }
}
