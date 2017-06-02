/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.dao.IfDiseaseDao;
import com.anggaranothing.pakar.dyslexia.model.Disease;
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
public class DiseaseDao extends DaoFactory implements IfDiseaseDao {

    // constructor
    public DiseaseDao( Connection conn ) throws RemoteException {
        super( conn );
        setAccessPrivilege( ServerConfig.getPrivilegeDisease() );
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
    public int create(Disease dssObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiseaseDao create() accessed !" );

        setSqlQuery("INSERT INTO data_penyakit " +
                    "(penyakit_id , penyakit_nama , penyakit_deskripsi , penyakit_pencegahan , penyakit_pengobatan , penyakit_gambar , penyakit_lastupdate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString(    1, dssObj.getId() );
            ps.setString(    2, dssObj.getName() );
            ps.setString(    3, dssObj.getDescription() );
            ps.setString(    4, dssObj.getPrevention() );
            ps.setString(    5, dssObj.getTreatment() );
            ps.setString(    6, dssObj.getPicture() );
            ps.setTimestamp( 7, dssObj.getLastUpdate() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "DiseaseDao create() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<Disease> read() throws RemoteException {
        defaultAccessChecking();
        
        List<Disease> objList = new ArrayList<Disease>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiseaseDao read() accessed !" );

        setSqlQuery("SELECT penyakit_id , penyakit_nama , penyakit_deskripsi , penyakit_pencegahan , penyakit_pengobatan , penyakit_gambar , penyakit_lastupdate " +
                    "FROM data_penyakit " +
                    "ORDER BY penyakit_id");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Disease obj = new Disease();
                obj.setId(          rs.getString(    "penyakit_id" ) );
                obj.setName(        rs.getString(    "penyakit_nama" ) );
                obj.setDescription( rs.getString(    "penyakit_deskripsi" ) );
                obj.setPrevention(  rs.getString(    "penyakit_pencegahan" ) );
                obj.setTreatment(   rs.getString(    "penyakit_pengobatan" ) );
                obj.setPicture(     rs.getString(    "penyakit_gambar" ) );
                obj.setLastUpdate(  rs.getTimestamp( "penyakit_lastupdate" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "DiseaseDao read() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }

    @Override
    public int update(Disease dssObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiseaseDao update() accessed !" );

        setSqlQuery("UPDATE data_penyakit SET"
                + " penyakit_nama = ?,"
                + " penyakit_deskripsi = ?,"
                + " penyakit_pencegahan = ?,"
                + " penyakit_pengobatan = ?,"
                + " penyakit_gambar = ?,"
                + " penyakit_lastupdate = ?"
                + " WHERE penyakit_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString(       1, dssObj.getName() );
            ps.setString(       2, dssObj.getDescription() );
            ps.setString(       3, dssObj.getPrevention() );
            ps.setString(       4, dssObj.getTreatment() );
            ps.setString(       5, dssObj.getPicture() );
            ps.setTimestamp(    6, dssObj.getLastUpdate() );
            ps.setString(       7, dssObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "DiseaseDao update() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public int delete(Disease dssObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiseaseDao delete() accessed !" );

        setSqlQuery("DELETE FROM data_penyakit " +
                    "WHERE penyakit_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, dssObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "DiseaseDao delete() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

}
