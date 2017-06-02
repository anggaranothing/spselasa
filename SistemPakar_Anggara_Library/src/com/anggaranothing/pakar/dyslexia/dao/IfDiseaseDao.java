/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.Disease;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author AnggaraNothing
 */
public interface IfDiseaseDao extends Remote {
    int             create( Disease dssObj )  throws RemoteException;
    List<Disease>   read( )                 throws RemoteException;
    int             update( Disease dssObj )  throws RemoteException;
    int             delete( Disease dssObj )  throws RemoteException;
}
