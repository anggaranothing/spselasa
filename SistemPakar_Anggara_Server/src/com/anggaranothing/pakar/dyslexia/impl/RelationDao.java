/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.server.Helper;
import com.anggaranothing.pakar.dyslexia.server.Main;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.anggaranothing.pakar.dyslexia.dao.IfRelationDao;
import com.anggaranothing.pakar.dyslexia.model.Relation;
import com.anggaranothing.pakar.dyslexia.model.Symptom;
import com.anggaranothing.pakar.dyslexia.model.Disease;
import com.anggaranothing.pakar.dyslexia.server.ServerConfig;


/**
 *
 * @author AnggaraNothing
 */
public class RelationDao extends DaoFactory implements IfRelationDao {

    public RelationDao(Connection conn) throws RemoteException {
        super(conn);
        setAccessPrivilege( ServerConfig.getPrivilegeRelation() );
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
    public int create( Relation rltObj ) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " RelationDao create() accessed !" );

        setSqlQuery("INSERT INTO data_relasi_gp " +
                    "(penyakit_id , gejala_id , relasi_cf) " +
                    "VALUES (?,?,?)");
        
        for ( Map.Entry<Symptom, Float> entry : rltObj.entrySet() ) {
            try {
                // buat objek PreparedStatement
                PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

                // set nilai parameter yg akan dikirim ke sql
                ps.setString(   1, rltObj.getDisease().getId() );
                ps.setString(   2, entry.getKey().getId() );
                ps.setFloat(    3, entry.getValue() );

                // jalankan perintah sql
                result = ps.executeUpdate();

            } catch (SQLException ex) {
                Main.LOGGER.error( "RelationDao create() is failed !", ex );
                throw new RemoteException( ex.getMessage() );
            }
        }
        
        return result;
    }

    @Override
    public List<Relation> read() throws RemoteException {
        defaultAccessChecking();
        
        List<Relation> objList = new ArrayList<Relation>();
        String lastDssId = "";
        Relation objRlt = null;
        
        Main.LOGGER.info( "["+getClientIp()+"]" + " RelationDao read() accessed !" );

        setSqlQuery("SELECT penyakit_id , penyakit_nama, gejala_id, gejala_deskripsi, cf " +
                    "FROM view_relasi_gp " +
                    "ORDER BY penyakit_id, gejala_id");
        
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                String dssId   = rs.getString( "penyakit_id" );
                String dssName = rs.getString( "penyakit_nama" );
                String smpId   = rs.getString( "gejala_id" );
                String smpDesc = rs.getString( "gejala_deskripsi" );
                Float rtlcf    = rs.getFloat(  "cf" );
                
                // Beda id penyakit?
                if( !lastDssId.equalsIgnoreCase( dssId ) ) {
                    //simpan data relasi terakhir jika bukan null
                    if( objRlt != null ) {
                        objList.add( objRlt );
                    }
                    
                    // Simpan id yang terakhir
                    lastDssId = dssId;
                    
                    // Buat objek relasi baru
                    objRlt = new Relation();
                    
                    // Buat objek disease
                    Disease objDss = new Disease();
                    objDss.setId(   dssId );
                    objDss.setName( dssName );
                    // Masukkan disease ke relasi
                    objRlt.setDisease( objDss );
                }
                
                // Buat objek symptom
                Symptom objSmp = new Symptom();
                objSmp.setId(          smpId );
                objSmp.setDescription( smpDesc );
                // Masukkan symptom dan nilai cf ke relasi
                objRlt.put( objSmp, rtlcf );
            }
            
            // Sudah tidak ada record lagi?
            // simpan data relasi terakhir jika bukan null
            if( objRlt != null ) {
                objList.add( objRlt );
            }
            
        } catch (SQLException ex) {
            Main.LOGGER.error( "RelationDao read() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }

    @Override
    public int update( Relation rltObj ) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " RelationDao update() accessed !" );

        setSqlQuery("UPDATE data_relasi_gp SET"
                + " relasi_cf = ?"
                + " WHERE penyakit_id = ? AND gejala_id = ?");
        
        for ( Map.Entry<Symptom, Float> entry : rltObj.entrySet() ) {
            try {
                // buat objek PreparedStatement
                PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

                // set nilai parameter yg akan dikirim ke sql
                ps.setFloat(  1, entry.getValue() );
                ps.setString( 2, rltObj.getDisease().getId() );
                ps.setString( 3, entry.getKey().getId() );

                // jalankan perintah sql
                result = ps.executeUpdate();

            } catch (SQLException ex) {
                Main.LOGGER.error( "RelationDao update() is failed !", ex );
                throw new RemoteException( ex.getMessage() );
            }
        }

        return result;
    }

    @Override
    public int delete( Relation rltObj ) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " RelationDao delete() accessed !" );

        setSqlQuery("DELETE FROM data_relasi_gp " +
                    "WHERE penyakit_id = ? AND gejala_id = ?");
        
        for ( Map.Entry<Symptom, Float> entry : rltObj.entrySet() ) {
            try {
                // buat objek PreparedStatement
                PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

                // set nilai parameter yg akan dikirim ke sql
                ps.setString( 1, rltObj.getDisease().getId() );
                ps.setString( 2, entry.getKey().getId() );

                // jalankan perintah sql
                result = ps.executeUpdate();

            } catch (SQLException ex) {
                Main.LOGGER.error( "RelationDao delete() is failed !", ex );
                throw new RemoteException( ex.getMessage() );
            }
        }

        return result;
    }

}
