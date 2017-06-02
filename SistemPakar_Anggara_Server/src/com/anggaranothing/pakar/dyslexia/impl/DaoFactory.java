/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.server.Main;
import com.anggaranothing.pakar.dyslexia.server.ServerConfig;
import java.rmi.RemoteException;
import static java.rmi.server.RemoteServer.getClientHost;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.Timestamp;

/**
 *
 * @author AnggaraNothing
 */
public abstract class DaoFactory extends UnicastRemoteObject {
    // deklarasi objek conn untuk menghandle koneksi ke database
    private Connection conn = null;
    private String sqlQuery;
    
    private int accessPrivilege = ServerConfig.getPrivilegeAdmin();
    
    // constructor
    public DaoFactory( Connection conn ) throws RemoteException {
        this.conn = conn;
    }
    
    /**
     * Get client ip address
     * @return the client ip address
    */
    public String getClientIp() {
        try {
            return getClientHost();
        } catch (ServerNotActiveException ex) {
            Main.LOGGER.trace( "Failed to retrieve client ip.", ex );
        }
        return null;
    }
    
    /**
     * Get current timestamp.
     * @return current timestamp.
    */
    public Timestamp getCurrentTimestamp() {
        return new Timestamp( System.currentTimeMillis() );
    }
    
    /**
     * Get RMI access privilege.
     * @return User group id
     */
    public final int getAccessPrivilege() {
        return accessPrivilege;
    }

    /**
     * Set RMI access privilege.
     * @param group_id Set to user group id
     */
    public final void setAccessPrivilege( int group_id ) {
        this.accessPrivilege = group_id;
    }

    abstract public boolean isPrivilegeGranted( );
    
    public final boolean isSessionExpired() {
        if( Main.SESSION.isSessionExpired( getClientIp() ) ) {
            Main.SESSION.removeLoggedUser( getClientIp() );
            return true;
        }
        return false;
    }
    
    /**
     * @return the conn
     */
    protected Connection getConn() {
        return conn;
    }

    /**
     * @return the sqlQuery
     */
    protected String getSqlQuery() {
        return sqlQuery;
    }
    
    /**
     * @param sqlQuery the sqlQuery to set
     */
    protected void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }
    
    /**
     * Execute default access checking.
     * @throws java.rmi.RemoteException
    */
    protected void defaultAccessChecking( ) throws RemoteException {
        if( !isPrivilegeGranted() )
            reportAccessViolation();
        
        if( isSessionExpired() )
            reportSessionExpired();
    }
    
    protected void reportAccessViolation() throws RemoteException {
        String message = String.format( "Access Denied ( User Group : %d , Privilege : %d )", ( Main.SESSION.isUserLogged(getClientIp()) ? Main.SESSION.getLoggedUser(getClientIp()).getGrup() : -1 ), getAccessPrivilege() );
        throw new RemoteException( message );
    }
    
    protected void reportSessionExpired() throws RemoteException {
        String message = String.format( "Session Expired" );
        throw new RemoteException( message );
    }
}
