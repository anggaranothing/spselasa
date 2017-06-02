/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.client;

import com.anggaranothing.pakar.dyslexia.Config;
import static com.anggaranothing.pakar.dyslexia.Config.getIniObj;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.ini4j.Profile;
import org.ini4j.Wini;

/**
 *
 * @author AnggaraNothing
 */
public class ClientConfig extends Config {
    
    protected static final String         RMI_USERLOG_NAME    = "auth";
    protected static final String         RMI_USER_NAME       = "usrDao";
    protected static final String         RMI_GROUP_NAME      = "usrGrpDao";
    protected static final String         RMI_SYMPTOM_NAME    = "symDao";
    protected static final String         RMI_DISEASE_NAME    = "dssDao";
    protected static final String         RMI_CFLEVEL_NAME    = "cflDao";
    protected static final String         RMI_RELATION_NAME   = "rtlDao";
    protected static final String         RMI_DIAGNOSTIC_NAME = "dgnDao";
    
    private static final String         INI_FILE             = "config.ini";
    private static final String         INI_DIR              = "./";
    private static final String         INI_PATH             = INI_DIR + INI_FILE;
    
    protected static final String       INI_RMI_HEADER       = "Client";
    protected static final String       INI_RMI_HOST         = "RMI_HOST";
    protected static final String       INI_RMI_PORT         = "RMI_PORT";
    
    protected static final String       INI_HEADER_DIAGNOSTIC       = "Diagnostic";
    protected static final String       INI_DIAGNOSTIC_MINPERCENTAGE = "MINPERCENTAGE";
        
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
    
    private static void storeDefaultConfigs() throws IOException {
        String[]  sectionArr = {
            INI_RMI_HEADER,
            INI_HEADER_DIAGNOSTIC
        };
        
        for( int i = 0; i < sectionArr.length; i++ ) {
            
            Profile.Section section = getIniObj().get( sectionArr[ i ] );
            if( section == null ) {
                section = getIniObj().add( sectionArr[ i ] );
            }
            
            switch( i ) {
                case 0 : {
                    section.putIfAbsent( INI_RMI_HOST, "localhost" );
                    section.putIfAbsent( INI_RMI_PORT, "1099" );
                    break;
                }
                case 1 : {
                    section.putIfAbsent(INI_DIAGNOSTIC_MINPERCENTAGE, "70.0" );
                    break;
                }
            }
            
        }
        
        iniObj.store();
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
    
    protected static String getRmiHost() {
        return read(INI_RMI_HEADER, INI_RMI_HOST );
    }
    
    protected static int getRmiPort() {
        return read(INI_RMI_HEADER, INI_RMI_PORT, int.class );
    }
    
    public static float getDiagnosticResultMinPercentage() {
        return parse(INI_HEADER_DIAGNOSTIC, INI_DIAGNOSTIC_MINPERCENTAGE , float.class );
    }
    
}
