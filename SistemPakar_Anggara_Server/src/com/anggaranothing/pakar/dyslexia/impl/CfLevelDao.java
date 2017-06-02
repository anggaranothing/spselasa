/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.dao.IfCfLevelDao;
import com.anggaranothing.pakar.dyslexia.model.CfLevel;
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
public class CfLevelDao extends DaoFactory implements IfCfLevelDao {

    public CfLevelDao(Connection conn) throws RemoteException {
        super(conn);
        setAccessPrivilege( ServerConfig.getPrivilegeCfLevel() );
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
    public int create(CfLevel cflObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " CFLevel create() accessed !" );

        setSqlQuery("INSERT INTO data_nilai_cf " +
                    "(value,description) " +
                    "VALUES (?,?)");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setFloat(  1, cflObj.getValue() );
            ps.setString( 2, cflObj.getDescription() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "CFLevel create() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<CfLevel> read() throws RemoteException {
        //defaultAccessChecking();
        boolean isUserGuest = !( Main.SESSION.isUserLogged( getClientIp() ) );
        if( isUserGuest )
            reportAccessViolation();
        
        if( isSessionExpired() )
            reportSessionExpired();
        
        List<CfLevel> objList = new ArrayList<CfLevel>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " CFLevel read() accessed !" );

        setSqlQuery("SELECT value, description " +
                    "FROM data_nilai_cf " +
                    "ORDER BY value");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                CfLevel obj = new CfLevel();
                obj.setValue(       rs.getFloat(     "value" ) );
                obj.setDescription( rs.getString(    "description" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "CFLevel read() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }

    @Override
    public int update(CfLevel cflObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " CFLevel update() accessed !" );

        setSqlQuery("UPDATE data_nilai_cf SET"
                + " description = ?"
                + " WHERE value = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString( 1, cflObj.getDescription() );
            ps.setDouble(2, cflObj.getValue() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "CFLevel update() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public int delete(CfLevel cflObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " CFLevel delete() accessed !" );

        setSqlQuery("DELETE FROM data_nilai_cf " +
                    "WHERE value = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setDouble(1, cflObj.getValue() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "CFLevel delete() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }
    
}
