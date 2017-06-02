/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.dao;

import com.anggaranothing.pakar.dyslexia.model.Symptom;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author AnggaraNothing
 */
public interface IfSymptomDao extends Remote {
    int          create( Symptom gejalaObj )  throws RemoteException;
    List<Symptom> read( )                     throws RemoteException;
    int          update( Symptom gejalaObj )  throws RemoteException;
    int          delete( Symptom gejalaObj )  throws RemoteException;
    
    List<Symptom> readOrderByCount( )         throws RemoteException;
}
