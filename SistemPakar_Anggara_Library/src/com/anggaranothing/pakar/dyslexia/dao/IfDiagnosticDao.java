/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.Diagnostic;
import com.anggaranothing.pakar.dyslexia.model.Disease;
import com.anggaranothing.pakar.dyslexia.model.Symptom;
import com.anggaranothing.pakar.dyslexia.model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AnggaraNothing
 */
public interface IfDiagnosticDao extends Remote {
    int                create( Diagnostic dgnObj )  throws RemoteException;
    List<Diagnostic>   read( )                      throws RemoteException;
    int                update( Diagnostic dgnObj )  throws RemoteException;
    int                delete( Diagnostic dgnObj )  throws RemoteException;
    
    List<Diagnostic>   readByUser( User user )      throws RemoteException;
    Map<Disease,Float> sendForm( Diagnostic dgnObj, Map<Symptom,Float> cflMap )  throws RemoteException;
    Map<Disease,Float> getResult( Diagnostic dgnObj )  throws RemoteException;
}
