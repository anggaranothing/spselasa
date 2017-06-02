/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.Relation;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author AnggaraNothing
 */
public interface IfRelationDao extends Remote {
    int             create( Relation rltObj )  throws RemoteException;
    List<Relation>  read( )                    throws RemoteException;
    int             update( Relation rltObj )  throws RemoteException;
    int             delete( Relation rltObj )  throws RemoteException;
}
