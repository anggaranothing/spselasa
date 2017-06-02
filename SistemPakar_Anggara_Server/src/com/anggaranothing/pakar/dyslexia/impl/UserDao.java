/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.dao.IfUserDao;
import com.anggaranothing.pakar.dyslexia.model.User;
import com.anggaranothing.pakar.dyslexia.server.Helper;
import com.anggaranothing.pakar.dyslexia.server.Main;
import com.anggaranothing.pakar.dyslexia.server.ServerConfig;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AnggaraNothing
 */
public class UserDao extends DaoFactory implements IfUserDao {

    // constructor
    public UserDao( Connection conn ) throws RemoteException {
        super( conn );
        setAccessPrivilege( ServerConfig.getPrivilegeUser() );
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
    public int create(User userObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " UserDao create() accessed !" );

        setSqlQuery("INSERT INTO data_user " +
                    "(user_email , user_nama , user_pass , user_female , grup_id , user_registerdate , user_lastlogindate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString(    1, userObj.getEmail() );
            ps.setString(    2, userObj.getName() );
            ps.setString(    3, userObj.getPass() );
            ps.setBoolean(   4, userObj.isFemale() );
            ps.setInt(       5, userObj.getGrup() );
            ps.setTimestamp( 6, userObj.getRegister() );
            ps.setTimestamp( 7, userObj.getLastlogin() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "UserDao create() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<User> read() throws RemoteException {
        defaultAccessChecking();
        
        List<User> objList = new ArrayList<User>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " UserDao read() accessed !" );

        setSqlQuery("SELECT user_id , user_email , user_nama , user_pass , user_female , grup_id , user_registerdate , user_lastlogindate " +
                    "FROM data_user " +
                    "ORDER BY user_id");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                User obj = new User();
                obj.setId(        rs.getInt(       "user_id" ) );
                obj.setEmail(     rs.getString(    "user_email" ) );
                obj.setName(      rs.getString(    "user_nama" ) );
                obj.setPass(      rs.getString(    "user_pass" ) );
                obj.setFemale(    rs.getBoolean(   "user_female" ) );
                obj.setGrup(      rs.getInt(       "grup_id" ) );
                obj.setRegister(  rs.getTimestamp( "user_registerdate" ) );
                obj.setLastlogin( rs.getTimestamp( "user_lastlogindate" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "UserDao read() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }

    @Override
    public int update(User userObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " UserDao update() accessed !" );

        setSqlQuery("UPDATE data_user SET"
                + " user_email = ?,"
                + " user_nama = ?,"
                + " user_pass = ?,"
                + " user_female = ?,"
                + " grup_id = ?,"
                + " user_registerdate = ?,"
                + " user_lastlogindate = ?"
                + " WHERE user_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString(    1, userObj.getEmail() );
            ps.setString(    2, userObj.getName() );
            ps.setString(    3, userObj.getPass() );
            ps.setBoolean(   4, userObj.isFemale() );
            ps.setInt(       5, userObj.getGrup() );
            ps.setTimestamp( 6, userObj.getRegister() );
            ps.setTimestamp( 7, userObj.getLastlogin() );
            ps.setLong(      8, userObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "UserDao update() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public int delete(User userObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " UserDao delete() accessed !" );

        setSqlQuery("DELETE FROM data_user " +
                    "WHERE user_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setLong( 1, userObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "UserDao delete() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

}
