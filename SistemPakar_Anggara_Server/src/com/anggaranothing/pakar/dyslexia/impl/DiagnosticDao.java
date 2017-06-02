/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.dao.IfDiagnosticDao;
import com.anggaranothing.pakar.dyslexia.model.Diagnostic;
import com.anggaranothing.pakar.dyslexia.model.Disease;
import com.anggaranothing.pakar.dyslexia.model.Symptom;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AnggaraNothing
 */
public class DiagnosticDao extends DaoFactory implements IfDiagnosticDao {

    // constructor
    public DiagnosticDao( Connection conn ) throws RemoteException {
        super( conn );
        setAccessPrivilege( ServerConfig.getPrivilegeDiagnostic() );
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
    public int create(Diagnostic dgnObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiagnosticDao create() accessed !" );

        setSqlQuery("INSERT INTO data_diagnosa " +
                    "(user_id , diagnosa_date , status , cf_user , cf_kombinasi , cf_persentase) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setLong(      1, dgnObj.getUser().getId() );
            ps.setTimestamp( 2, dgnObj.getDate() );
            ps.setString(    3, dgnObj.getStatus().name() );
            ps.setString(    4, dgnObj.getCfUser() );
            ps.setString(    5, dgnObj.getCfCombined() );
            ps.setString(    6, dgnObj.getCfPercentage() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "DiagnosticDao create() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<Diagnostic> read() throws RemoteException {
        defaultAccessChecking();
        
        List<Diagnostic> objList = new ArrayList<Diagnostic>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiagnosticDao read() accessed !" );

        setSqlQuery("SELECT diagnosa_id , user_id , user_nama , diagnosa_date , status , cf_user , cf_kombinasi , cf_persentase " +
                    "FROM view_diagnosa " +
                    "ORDER BY diagnosa_id");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Diagnostic obj = new Diagnostic();
                
                obj.setId(           rs.getLong(      "diagnosa_id" ) );
                
                User objUser = new User();
                objUser.setId(       rs.getLong(      "user_id" ) );
                objUser.setName(     rs.getString(    "user_nama" ) );
                obj.setUser(         objUser );
                
                obj.setDate(         rs.getTimestamp( "diagnosa_date" ) );
                obj.setStatus(       Diagnostic.StatusEnum.valueOf( rs.getString( "status" ) ) );
                obj.setCfUser(       rs.getString(    "cf_user" ) );
                obj.setCfCombined(   rs.getString(    "cf_kombinasi" ) );
                obj.setCfPercentage( rs.getString(    "cf_persentase" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "DiagnosticDao read() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }

    @Override
    public int update(Diagnostic dgnObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiagnosticDao update() accessed !" );

        setSqlQuery("UPDATE data_diagnosa SET"
                + " user_id = ?,"
                + " diagnosa_date = ?,"
                + " status = ?,"
                + " cf_user = ?,"
                + " cf_kombinasi = ?,"
                + " cf_persentase = ?"
                + " WHERE diagnosa_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setLong(      1, dgnObj.getUser().getId() );
            ps.setTimestamp( 2, dgnObj.getDate() );
            ps.setString(    3, dgnObj.getStatus().name() );
            ps.setString(    4, dgnObj.getCfUser() );
            ps.setString(    5, dgnObj.getCfCombined() );
            ps.setString(    6, dgnObj.getCfPercentage() );
            ps.setLong(      7, dgnObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "DiagnosticDao update() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public int delete(Diagnostic dgnObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiagnosticDao delete() accessed !" );

        setSqlQuery("DELETE FROM data_diagnosa " +
                    "WHERE diagnosa_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setLong( 1, dgnObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "DiagnosticDao delete() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<Diagnostic> readByUser(User user) throws RemoteException {
        //defaultAccessChecking();
        boolean isUserGuest = !( Main.SESSION.isUserLogged( getClientIp() ) );
        if( isUserGuest )
            reportAccessViolation();
        
        if( isSessionExpired() )
            reportSessionExpired();

        List<Diagnostic> objList = new ArrayList<Diagnostic>();
        
        Main.LOGGER.info( "["+getClientIp()+"]" + " DiagnosticDao readByUser() accessed !" );

        setSqlQuery("SELECT diagnosa_id , user_id , user_nama , diagnosa_date , status , cf_user , cf_kombinasi , cf_persentase " +
                    "FROM view_diagnosa " +
                    "WHERE user_id=? " +
                    "ORDER BY diagnosa_id" );
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            ps.setLong( 1, user.getId() );
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Diagnostic obj = new Diagnostic();
                
                obj.setId(           rs.getLong(      "diagnosa_id" ) );
                
                User objUser = new User();
                objUser.setId(       rs.getLong(      "user_id" ) );
                objUser.setName(     rs.getString(    "user_nama" ) );
                obj.setUser(         objUser );
                
                obj.setDate(         rs.getTimestamp( "diagnosa_date" ) );
                obj.setStatus(       Diagnostic.StatusEnum.valueOf( rs.getString( "status" ) ) );
                obj.setCfUser(       rs.getString(    "cf_user" ) );
                obj.setCfCombined(   rs.getString(    "cf_kombinasi" ) );
                obj.setCfPercentage( rs.getString(    "cf_persentase" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "DiagnosticDao readByUser() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }
    
    @Override
    public Map<Disease,Float> sendForm(Diagnostic dgnObj, Map<Symptom, Float> cflMap) throws RemoteException {
        //defaultAccessChecking();
        boolean isUserGuest = !( Main.SESSION.isUserLogged( getClientIp() ) );
        if( isUserGuest )
            reportAccessViolation();
        
        if( isSessionExpired() )
            reportSessionExpired();
        
        int result = 0;
        
        Main.LOGGER.info( "["+getClientIp()+"]" + " DiagnosticDao sendForm() accessed !" );
        
        Map<Disease,Float> temp = null;
        
        if( cflMap != null && cflMap.isEmpty() == false ) {
            try {
                temp = processForm( dgnObj, cflMap );
            } catch (SQLException ex) {
                Main.LOGGER.error( "DiagnosticDao sendForm() is failed !", ex );
                throw new RemoteException( ex.getMessage() );
            }
        }
        
        setSqlQuery("INSERT INTO data_diagnosa " +
                    "(user_id , diagnosa_date , status , cf_user , cf_kombinasi , cf_persentase) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setLong(      1, dgnObj.getUser().getId() );
            ps.setTimestamp( 2, dgnObj.getDate() );
            ps.setString(    3, dgnObj.getStatus().name() );
            ps.setString(    4, dgnObj.getCfUser() );
            ps.setString(    5, dgnObj.getCfCombined() );
            ps.setString(    6, dgnObj.getCfPercentage() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "DiagnosticDao sendForm() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return temp;
    }
    
    private Map<Disease,Float> processForm(Diagnostic dgnObj, Map<Symptom, Float> cflMap) throws SQLException {
        // Mapping kembali cfUser dengan symptom id sebagai key
        HashMap<String,Float> cfUser = new HashMap<String,Float>();
        
        // Daftar CFPakar <IdPenyakit , < IdGejala , CF >>
        HashMap<String,HashMap<String,Float>> cfPakar = new HashMap<String,HashMap<String,Float>>();
        
        // Hasil perkalian cfPakar dengan cfUser <IdPenyakit , CF>
        HashMap<String,List<Float>> cfKali  = new HashMap<String,List<Float>>();  
        
        // Hasil kombinasi cfKali <IdPenyakit , CF>
        HashMap<String,Float>  cfKombinasi  = new HashMap<String,Float>();  
        
        // Produk akhir
        HashMap<Disease,Float>  cfResult   = new HashMap<Disease,Float>();
        
        // Mapping kembali cfUser dengan symptom id sebagai key
        for( Map.Entry<Symptom,Float> entry : cflMap.entrySet() ) {
            cfUser.put( entry.getKey().getId(), entry.getValue() );
        }
        
        // Mulai cari cfPakar dari relasi gejala dengan penyakit
        for( Map.Entry<String,Float> entry : cfUser.entrySet() ) {
            setSqlQuery( "SELECT penyakit_id, relasi_cf " +
                         "FROM data_relasi_gp " +
                         "WHERE gejala_id=?"
                   );
            
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());
            ps.setString( 1, entry.getKey() );
            ResultSet rs = ps.executeQuery();

            // Masukkan ke daftar cfPakar
            while (rs.next()) {
                String idPenyakit = rs.getString( "penyakit_id" );
                String idGejala   = entry.getKey();
                float  relasiCf   = rs.getFloat(  "relasi_cf" );

                HashMap<String,Float> cfGejala = new HashMap<String,Float>();
                // sudah ada data sbelomnya?
                if( cfPakar.containsKey( idPenyakit ) ) {
                    cfGejala = cfPakar.get( idPenyakit );
                }
                cfGejala.put( idGejala, relasiCf );
                cfPakar.put(  idPenyakit, cfGejala );
            }
        }
        
        // Mari menghitung CF :yay:
        for( Map.Entry<String,HashMap<String,Float>> entryPakar : cfPakar.entrySet() ) {
            
            List<Float> cfExpertUser = new ArrayList<Float>();    
            String idPenyakit = entryPakar.getKey();
            
            // Hitung perkalian cfPakar dengan cfUser
            for( Map.Entry<String,Float> entryGejala : entryPakar.getValue().entrySet() ) {
                // cfPakar kali cfUser
                cfExpertUser.add( InferenceEngine.cfHE( entryGejala.getValue() , cfUser.get( entryGejala.getKey() ) ) );
            }
            
            // setor cfKali ke id penyakit masing-masing
            cfKali.put( idPenyakit, cfExpertUser );
            
            // Hitung cf kombinasi
            float hasilKombinasi = cfExpertUser.get( 0 );
            for( int i = 1; i < cfExpertUser.size(); i++ ) {
                hasilKombinasi = InferenceEngine.cfCombine( hasilKombinasi, cfExpertUser.get(i) );
            }
            // setor cfKombinasi ke setiap id penyakit
            cfKombinasi.put( idPenyakit, hasilKombinasi );
            
            // Peroleh data penyakit lalu setor beserta persentase nya
            setSqlQuery("SELECT penyakit_id , penyakit_nama , penyakit_deskripsi , penyakit_pencegahan , penyakit_pengobatan , penyakit_gambar , penyakit_lastupdate " +
                        "FROM data_penyakit " +
                        "WHERE penyakit_id=?");
            
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            ps.setString( 1, idPenyakit );

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // mapping objek ResultSet ke object barang
                Disease obj = new Disease();
                obj.setId(          rs.getString(    "penyakit_id" ) );
                obj.setName(        rs.getString(    "penyakit_nama" ) );
                obj.setDescription( rs.getString(    "penyakit_deskripsi" ) );
                obj.setPrevention(  rs.getString(    "penyakit_pencegahan" ) );
                obj.setTreatment(   rs.getString(    "penyakit_pengobatan" ) );
                obj.setPicture(     rs.getString(    "penyakit_gambar" ) );
                obj.setLastUpdate(  rs.getTimestamp( "penyakit_lastupdate" ) );

                // simpan persentase ke dalam List
                cfResult.put( obj, hasilKombinasi*100 );
            }
        }
        
        // Okay, saat nya set cfKombinasi dan cfPersentase ke objek Diagnostic
        String cmb = "",
               prc = "";
        for( Map.Entry<String,Float> entry : cfKombinasi.entrySet()) {
            cmb += String.format( "%s=%.2f ", entry.getKey(), entry.getValue() );
            prc += String.format( "%s=%.2f ", entry.getKey(), entry.getValue()*100 );
        }
        dgnObj.setCfCombined(   cmb );
        dgnObj.setCfPercentage( prc );
        
        return cfResult;
    }

    @Override
    public Map<Disease, Float> getResult(Diagnostic dgnObj) throws RemoteException {
        //defaultAccessChecking();
        boolean isUserGuest = !( Main.SESSION.isUserLogged( getClientIp() ) );
        if( isUserGuest )
            reportAccessViolation();
        
        if( isSessionExpired() )
            reportSessionExpired();
        
        Map<Disease, Float> resultMap = new LinkedHashMap<Disease, Float>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " DiagnosticDao getResult() accessed !" );

        setSqlQuery("SELECT cf_persentase " +
                    "FROM view_diagnosa " +
                    "WHERE diagnosa_id=?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            ps.setLong( 1, dgnObj.getId() );
            
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String cfPersen = rs.getString( "cf_persentase" );
                
                if( cfPersen.trim().isEmpty() == false && cfPersen.trim().equalsIgnoreCase("cancelled") == false ) {
                    // pecah string dgn space sbg deliminator
                    for( String persenPenyakit : cfPersen.split(" ") ){
                        Disease dssObj = new Disease();
                        String idPenyakit = persenPenyakit.split( "=" )[0],
                               persen     = persenPenyakit.split( "=" )[1];

                        // Peroleh data penyakit
                        setSqlQuery("SELECT penyakit_id , penyakit_nama , penyakit_deskripsi , penyakit_pencegahan , penyakit_pengobatan , penyakit_gambar , penyakit_lastupdate " +
                                    "FROM data_penyakit " +
                                    "WHERE penyakit_id=?");
                        try {
                            // buat objek PreparedStatement
                            ps = getConn().prepareStatement(getSqlQuery());

                            ps.setString( 1, idPenyakit );

                            // buat objek ResultSet utk menampung hasil SELECT
                            ResultSet rs2 = ps.executeQuery();
                            if (rs2.next()) {
                                // mapping objek ResultSet ke object barang
                                Disease obj = new Disease();
                                dssObj.setId(          rs2.getString(    "penyakit_id" ) );
                                dssObj.setName(        rs2.getString(    "penyakit_nama" ) );
                                dssObj.setDescription( rs2.getString(    "penyakit_deskripsi" ) );
                                dssObj.setPrevention(  rs2.getString(    "penyakit_pencegahan" ) );
                                dssObj.setTreatment(   rs2.getString(    "penyakit_pengobatan" ) );
                                dssObj.setPicture(     rs2.getString(    "penyakit_gambar" ) );
                                dssObj.setLastUpdate(  rs2.getTimestamp( "penyakit_lastupdate" ) );
                            }
                        } catch (SQLException ex) {
                            Main.LOGGER.error( "DiagnosticDao getResult() is failed !", ex );
                            throw new RemoteException( ex.getMessage() );
                        }

                        resultMap.put( dssObj, Float.parseFloat( persen.replaceAll( ",", ".") ) );
                    }
                }
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "DiagnosticDao getResult() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return resultMap;
    }
}
