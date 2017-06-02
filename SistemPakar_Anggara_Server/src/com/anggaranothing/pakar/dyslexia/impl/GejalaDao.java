/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.model.Symptom;
import com.anggaranothing.pakar.dyslexia.server.Helper;
import com.anggaranothing.pakar.dyslexia.server.Main;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.anggaranothing.pakar.dyslexia.dao.IfSymptomDao;
import com.anggaranothing.pakar.dyslexia.server.ServerConfig;

/**
 *
 * @author AnggaraNothing
 */
public class GejalaDao extends DaoFactory implements IfSymptomDao {

    public GejalaDao(Connection conn) throws RemoteException {
        super(conn);
        setAccessPrivilege( ServerConfig.getPrivilegeSymptom() );
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
    public int create(Symptom gejalaObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " GejalaDao create() accessed !" );

        setSqlQuery("INSERT INTO data_gejala " +
                    "(gejala_id , gejala_deskripsi) " +
                    "VALUES (?,?)");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString( 1, gejalaObj.getId() );
            ps.setString( 2, gejalaObj.getDescription() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "GejalaDao create() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<Symptom> read() throws RemoteException {
        defaultAccessChecking();
        
        List<Symptom> objList = new ArrayList<Symptom>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " GejalaDao read() accessed !" );

        setSqlQuery("SELECT gejala_id , gejala_deskripsi " +
                    "FROM data_gejala " +
                    "ORDER BY gejala_id");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Symptom obj = new Symptom();
                obj.setId(          rs.getString(    "gejala_id" ) );
                obj.setDescription( rs.getString(    "gejala_deskripsi" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "GejalaDao read() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }

    @Override
    public int update(Symptom gejalaObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " GejalaDao update() accessed !" );

        setSqlQuery("UPDATE data_gejala SET"
                + " gejala_deskripsi = ?"
                + " WHERE gejala_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString(    1, gejalaObj.getDescription() );
            ps.setString(    2, gejalaObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "GejalaDao update() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public int delete(Symptom gejalaObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " GejalaDao delete() accessed !" );

        setSqlQuery("DELETE FROM data_gejala " +
                    "WHERE gejala_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString( 1, gejalaObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "GejalaDao delete() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<Symptom> readOrderByCount() throws RemoteException {
        boolean isUserGuest = !( Main.SESSION.isUserLogged( getClientIp() ) );
        if( isUserGuest )
            reportAccessViolation();
        
        if( isSessionExpired() )
            reportSessionExpired();
        
        List<Symptom> objList = new ArrayList<Symptom>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " GejalaDao readOrderByCount() accessed !" );

        setSqlQuery("SELECT gejala_id , gejala_deskripsi " +
                    "FROM view_gejala_count ");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Symptom obj = new Symptom();
                obj.setId(          rs.getString(    "gejala_id" ) );
                obj.setDescription( rs.getString(    "gejala_deskripsi" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "GejalaDao readOrderByCount() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }
    
}
