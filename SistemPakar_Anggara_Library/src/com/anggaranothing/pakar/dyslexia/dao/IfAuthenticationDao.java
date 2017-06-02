/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author AnggaraNothing
 */
public interface IfAuthenticationDao extends Remote {
    boolean     isLogged() throws RemoteException ;
    boolean     login( String userEmail, String password ) throws RemoteException;
    boolean     logout( ) throws RemoteException;
    User        getUser( ) throws RemoteException;
}
