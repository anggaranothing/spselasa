/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author AnggaraNothing
 */
public interface IfUserDao extends Remote {
    int         create( User userObj )  throws RemoteException;
    List<User>  read( )                 throws RemoteException;
    int         update( User userObj )  throws RemoteException;
    int         delete( User userObj )  throws RemoteException;
}
