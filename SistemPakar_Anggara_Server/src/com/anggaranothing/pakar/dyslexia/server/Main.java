/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.server;

import com.anggaranothing.pakar.dyslexia.impl.UserDao;
import com.anggaranothing.pakar.dyslexia.impl.AuthenticationDao;
import com.anggaranothing.pakar.dyslexia.impl.CfLevelDao;
import com.anggaranothing.pakar.dyslexia.impl.DiagnosticDao;
import com.anggaranothing.pakar.dyslexia.impl.DiseaseDao;
import com.anggaranothing.pakar.dyslexia.impl.GejalaDao;
import com.anggaranothing.pakar.dyslexia.impl.GroupDao;
import com.anggaranothing.pakar.dyslexia.impl.RelationDao;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AnggaraNothing
 */
public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger( Main.class );
    
    public static final String APP_NAME = "Sistem Pakar Dyslexia \"SELASA\" Server";
    public static final String APP_VER  = "1.0.0 Beta";
    
    public static final Session SESSION = new Session();
    
    public static String getAppInfo() {
        return String.format( "%s %s" , APP_NAME, APP_VER );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if( !ServerConfig.Init() )
            System.exit( ErrorCode.CONFIG_FAILED_LOAD );
        
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName( ServerConfig.getDriverClassname() ).newInstance();
            
            try {
                // buat objek Connection
                Connection conn = DriverManager.getConnection( ServerConfig.getDriverUrl(), ServerConfig.getDatabaseUser(), ServerConfig.getDatabasePassword() );
                        
                try {
                    // buat objek RMI registry menggunakan port default RMI (1099)
                    Registry registry = LocateRegistry.createRegistry( ServerConfig.getRmiPort() );
                    
                    // buat objek DAO
                    AuthenticationDao   authDao     = new AuthenticationDao( conn );
                    UserDao             userDao     = new UserDao( conn );
                    GroupDao            groupDao    = new GroupDao( conn );
                    GejalaDao           gejalaDao   = new GejalaDao( conn );
                    DiseaseDao          dssDao      = new DiseaseDao( conn );
                    CfLevelDao          cflDao      = new CfLevelDao( conn );
                    RelationDao         rtlDao      = new RelationDao( conn );
                    DiagnosticDao       dgnDao      = new DiagnosticDao( conn );

                    // mendaftarkan objek DAO ke RMI Registry
                    registry.rebind( "auth",        authDao );
                    registry.rebind( "usrDao",      userDao );
                    registry.rebind( "usrGrpDao",   groupDao );
                    registry.rebind( "symDao",      gejalaDao );
                    registry.rebind( "dssDao",      dssDao );
                    registry.rebind( "cflDao",      cflDao );
                    registry.rebind( "rtlDao",      rtlDao );
                    registry.rebind( "dgnDao",      dgnDao );

                    LOGGER.info(String.format("%s is RUNNING !" , getAppInfo() ) );
                } catch (RemoteException ex) {
                    LOGGER.error( "RMI registry error !" , ex );
                }
            } catch (SQLException ex) {
                LOGGER.error( "Failed to connect to database!", ex );
            }
        } catch (ClassNotFoundException ex) {
            Helper.logThrowable( "Database driver not found!", ex );
        } catch (InstantiationException ex) {
            Helper.logThrowable( "Database driver is failed!", ex );
        } catch (IllegalAccessException ex) {
            Helper.logThrowable( "Database driver is failed!", ex );
        }
    }
}
