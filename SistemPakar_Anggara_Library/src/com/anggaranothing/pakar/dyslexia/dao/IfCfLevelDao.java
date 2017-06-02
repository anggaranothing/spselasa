/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.CfLevel;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author AnggaraNothing
 */
public interface IfCfLevelDao extends Remote {
    int             create( CfLevel cflObj )  throws RemoteException;
    List<CfLevel>   read( )                   throws RemoteException;
    int             update( CfLevel cflObj )  throws RemoteException;
    int             delete( CfLevel cflObj )  throws RemoteException;
}
