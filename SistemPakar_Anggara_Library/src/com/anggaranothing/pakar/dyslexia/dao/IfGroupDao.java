/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.Group;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author AnggaraNothing
 */
public interface IfGroupDao extends Remote {
    int         create( Group groupObj )  throws RemoteException;
    List<Group> read( )                   throws RemoteException;
    int         update( Group groupObj )  throws RemoteException;
    int         delete( Group groupObj )  throws RemoteException;
}
