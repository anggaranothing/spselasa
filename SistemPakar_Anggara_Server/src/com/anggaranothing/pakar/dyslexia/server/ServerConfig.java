/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.server;

import com.anggaranothing.pakar.dyslexia.Config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

/**
 *
 * @author AnggaraNothing
 */
public class ServerConfig extends Config {    
    private static final String         INI_FILE             = "config.ini";
    private static final String         INI_DIR              = "./";
    private static final String         INI_PATH             = INI_DIR + INI_FILE;
    
    private static final String         INI_HEADER_DRIVER    = "Driver";
    private static final String         INI_DRIVER_CLASSNAME = "DB_DRIVER_CLASSNAME";
    private static final String         INI_DRIVER_URL       = "DB_URL";
    
    private static final String         INI_HEADER_DB        = "Server";
    private static final String         INI_DB_HOST          = "DB_HOST";
    private static final String         INI_DB_PORT          = "DB_PORT";
    private static final String         INI_DB_NAME          = "DB_NAME";
    private static final String         INI_DB_USER          = "DB_USER";
    private static final String         INI_DB_PASS          = "DB_PASS";
    private static final String         INI_RMI_PORT         = "RMI_PORT";
    
    private static final String         INI_HEADER_CLIENT    = "Client";
    private static final String         INI_CLIENT_TIMEOUT   = "SESSION_TIMEOUT";
    
    private static final String         INI_HEADER_PRIVILEGE = "Privilege";
    private static final String         INI_PRIVILEGE_ADMIN  = "ADMIN_ID";
    private static final String         INI_PRIVILEGE_AUTH   = "AUTH_ID";
    private static final String         INI_PRIVILEGE_USER   = "USER_ID";
    private static final String         INI_PRIVILEGE_GROUP  = "GROUP_ID";
    private static final String         INI_PRIVILEGE_SYMP   = "SYMPTOM_ID";
    private static final String         INI_PRIVILEGE_DSS    = "DISEASE_ID";
    private static final String         INI_PRIVILEGE_CFL    = "CFLEVEL_ID";
    private static final String         INI_PRIVILEGE_RTL    = "RELATION_ID";
    private static final String         INI_PRIVILEGE_DGN    = "DIAGNOSTIC_ID";
    
    protected static boolean Init() {
        if( createIniFile() ) {
            try {
                iniObj = new Wini( fileObj );
                storeDefaultConfigs();
                return true;
            } catch (FileNotFoundException ex) {
                Helper.logThrowable( "Config file failed to load !", ex );
            } catch (IOException ex) {
                Helper.logThrowable( "Config file failed to load !", ex );
            }
        }
        return false;
    }
    
    private static boolean createIniFile() {
        try {
            fileObj = new File( INI_DIR , INI_FILE );
            if ( fileObj.createNewFile() ){
                Main.LOGGER.debug( "Config file is created !" );
            }
            else {
                Main.LOGGER.debug( "Config file already exists !" );
            }
            fileObj.setReadable(true, true);
            fileObj.setWritable(true, true);
            return true;
        } catch (IOException ex) {
            Helper.logThrowable( "Config file failed to create !", ex );
            return false;
        }
    }
    
    private static void storeDefaultConfigs() throws IOException {
        String[]  sectionArr = {
            INI_HEADER_DRIVER,
            INI_HEADER_DB,
            INI_HEADER_CLIENT,
            INI_HEADER_PRIVILEGE
        };
        
        for( int i = 0; i < sectionArr.length; i++ ) {
            
            Section section = getIniObj().get( sectionArr[ i ] );
            if( section == null ) {
                section = getIniObj().add( sectionArr[ i ] );
            }
            
            switch( i ) {
                case 0 : {
                    section.putIfAbsent( INI_DRIVER_CLASSNAME, "com.mysql.jdbc.Driver" );
                    section.putIfAbsent( INI_DRIVER_URL,       "jdbc:mysql://${Server/DB_HOST}:${Server/DB_PORT}/${Server/DB_NAME}?autoReconnect=true" );
                    break;
                }
                case 1 : {
                    section.putIfAbsent( INI_DB_HOST,  "localhost" );
                    section.putIfAbsent( INI_DB_PORT,  "3306" );
                    section.putIfAbsent( INI_DB_NAME,  "db_spa" );
                    section.putIfAbsent( INI_DB_USER,  "spa_user" );
                    section.putIfAbsent( INI_DB_PASS,  "" );
                    section.putIfAbsent( INI_RMI_PORT, "1099" );
                    break;
                }
                case 2 : {
                    section.putIfAbsent( INI_CLIENT_TIMEOUT, "600" );
                    break;
                }
                case 3 : {
                    section.putIfAbsent( INI_PRIVILEGE_ADMIN,   "1" );
                    section.putIfAbsent( INI_PRIVILEGE_AUTH,    "0" );
                    section.putIfAbsent( INI_PRIVILEGE_USER,    "${Privilege/ADMIN_ID}" );
                    section.putIfAbsent( INI_PRIVILEGE_GROUP,   "${Privilege/ADMIN_ID}" );
                    section.putIfAbsent( INI_PRIVILEGE_SYMP,    "2" );
                    section.putIfAbsent( INI_PRIVILEGE_DSS,     "2" );
                    section.putIfAbsent( INI_PRIVILEGE_CFL,     "2" );
                    section.putIfAbsent( INI_PRIVILEGE_RTL,     "2" );
                    section.putIfAbsent( INI_PRIVILEGE_DGN,     "2" );
                    break;
                }
            }
            
        }
        
        iniObj.store();
    }
    
    public static String getDriverClassname() {
        return read( INI_HEADER_DRIVER, INI_DRIVER_CLASSNAME );
    }
    
    public static String getDriverUrl() {
        return parse( INI_HEADER_DRIVER, INI_DRIVER_URL );
    }
    
    public static String getDatabaseHost() {
        return read( INI_HEADER_DB, INI_DB_HOST );
    }
    
    public static int getDatabasePort() {
        return read( INI_HEADER_DB, INI_DB_PORT, int.class );
    }
    
    public static String getDatabaseName() {
        return read( INI_HEADER_DB, INI_DB_NAME );
    }
    
    public static String getDatabaseUser() {
        return read( INI_HEADER_DB, INI_DB_USER );
    }
    
    public static String getDatabasePassword() {
        return read( INI_HEADER_DB, INI_DB_PASS );
    }
    
    public static int getRmiPort() {
        return read( INI_HEADER_DB, INI_RMI_PORT, int.class );
    }
    
    public static int getClientSessionTimeout() {
        return read( INI_HEADER_CLIENT, INI_CLIENT_TIMEOUT, int.class );
    }
    
    public static int getPrivilegeAdmin() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_ADMIN , int.class );
    }
    
    public static int getPrivilegeAuth() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_AUTH , int.class );
    }
    
    public static int getPrivilegeUser() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_USER , int.class );
    }
    
    public static int getPrivilegeGroup() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_GROUP , int.class );
    }
    
    public static int getPrivilegeSymptom() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_SYMP , int.class );
    }
    
    public static int getPrivilegeDisease() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_DSS , int.class );
    }
    public static int getPrivilegeCfLevel() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_CFL , int.class );
    }
    
    public static int getPrivilegeRelation() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_RTL , int.class );
    }
    
    public static int getPrivilegeDiagnostic() {
        return parse( INI_HEADER_PRIVILEGE, INI_PRIVILEGE_DGN , int.class );
    }
    
}
