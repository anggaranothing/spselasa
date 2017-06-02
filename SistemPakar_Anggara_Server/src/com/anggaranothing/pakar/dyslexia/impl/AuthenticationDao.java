/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.model.User;
import com.anggaranothing.pakar.dyslexia.server.Main;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.anggaranothing.pakar.dyslexia.dao.IfAuthenticationDao;
import com.anggaranothing.pakar.dyslexia.server.Helper;
import com.anggaranothing.pakar.dyslexia.server.ServerConfig;

/**
 *
 * @author AnggaraNothing
 */
public class AuthenticationDao extends DaoFactory implements IfAuthenticationDao {

    public AuthenticationDao(Connection conn) throws RemoteException {
        super(conn);
        setAccessPrivilege( ServerConfig.getPrivilegeAuth() );
    }

    @Override
    public boolean isPrivilegeGranted() {
        return getAccessPrivilege() == Helper.GROUP_ALL || 
                ( Main.SESSION.isUserLogged( getClientIp() ) && 
                 ( Main.SESSION.getLoggedUser( getClientIp() ).getGrup() == ServerConfig.getPrivilegeAdmin() || 
                   Main.SESSION.getLoggedUser( getClientIp() ).getGrup() == getAccessPrivilege()
                 )
                );
    }
    
    @Override
    public boolean isLogged() {
        return Main.SESSION.isUserLogged( getClientIp() );
    }
    
    /**
     * Check if password is match.
     * @param value the entered password
     * @param toCompare the origin password to compare with
     * @return True if password is match.
     */
    private boolean isPasswordMatch( String str1, String str2 ) {
        str1 = ( str1 == null ) ? "" : str1;
        str2 = ( str2 == null ) ? "" : str2;
        
        return str2.equals( str1 );
    }
    
    private int updateLastLogin( User userObj ) throws RemoteException {
        int result = 0;
        
        userObj.setLastlogin( getCurrentTimestamp() );
        
        String sqlQuery = "UPDATE view_loguser SET"
                        + " lastlogin = ?"
                        + " WHERE id  = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement( sqlQuery );

            // set nilai parameter yg akan dikirim ke sql
            ps.setTimestamp( 1, userObj.getLastlogin() );
            ps.setLong(      2, userObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "UserLogDao updateLastLogin() is failed !", ex );
        }

        return result;
    }
    
    private User readByEmail(String userEmail) {
        User obj = null;

        String sqlQuery = "SELECT id, email, nama, pass, grup_id, register, lastlogin " +
                          "FROM view_loguser " +
                          "WHERE email = ?" +
                          "ORDER BY id";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement( sqlQuery );

            // set nilai parameter yg akan dikirim ke sql
            ps.setString( 1, userEmail );
            
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            if ( rs.next() ) {
                // simpan objek barang ke dalam objek class List
                obj = new User();
                obj.setId(        rs.getInt(       "id" ) );
                obj.setEmail(     rs.getString(    "email" ) );
                obj.setName(      rs.getString(    "nama" ) );
                obj.setPass(      rs.getString(    "pass" ) );
                obj.setGrup(      rs.getInt(       "grup_id" ) );
                obj.setRegister(  rs.getTimestamp( "register" ) );
                obj.setLastlogin( rs.getTimestamp( "lastlogin" ) );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "UserLoginDao readByName() is failed !", ex );
        }

        return obj;
    }

    @Override
    public boolean login(String userEmail, String password) throws RemoteException {
        User model = readByEmail( userEmail );
            
        if( model != null ) {
            if( isPasswordMatch( model.getPass() , password ) ) {
                if( Main.SESSION.isUserLogged( model ) ) {
                    throw new RemoteException( "User already logged in by another client" );
                }
                else {
                    updateLastLogin( model );
                    Main.SESSION.addLoggedUser( getClientIp(), model );
                    Main.LOGGER.info( "["+getClientIp()+"] SUCCESSFULLY login as " + userEmail );
                    return true;
                }
            }
            /*else {
                throw new RemoteException( "Wrong Password" );
            }*/
        }
        
        Main.LOGGER.info( "["+getClientIp()+"] is FAILED to login as " + userEmail );
        return false;
    }

    @Override
    public boolean logout() throws RemoteException {
        Main.LOGGER.info( "["+getClientIp()+"] logged out." );
        return Main.SESSION.removeLoggedUser( getClientIp() );
    }

    @Override
    public User getUser() throws RemoteException {
        return Main.SESSION.getLoggedUser( getClientIp() );
    }

}
