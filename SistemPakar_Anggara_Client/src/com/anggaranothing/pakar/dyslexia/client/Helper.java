/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.client;

import java.awt.Component;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
import com.anggaranothing.pakar.dyslexia.dao.IfAuthenticationDao;
import com.anggaranothing.pakar.dyslexia.model.User;
import java.sql.Timestamp;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author AnggaraNothing
 */
public class Helper extends com.anggaranothing.pakar.dyslexia.Helper {
    
    private static final String Digits     = "(\\p{Digit}+)";
    private static final String HexDigits  = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally 
    // signed decimal integer.
    private static final String Exp        = "[eE][+-]?"+Digits;
    private static final String fpRegex    =
        ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
        "[+-]?(" +         // Optional sign character
        "NaN|" +           // "NaN" string
        "Infinity|" +      // "Infinity" string

        // A decimal floating-point string representing a finite positive
        // number without a leading sign has at most five basic pieces:
        // Digits . Digits ExponentPart FloatTypeSuffix
        // 
        // Since this method allows integer-only strings as input
        // in addition to strings of floating-point literals, the
        // two sub-patterns below are simplifications of the grammar
        // productions from the Java Language Specification, 2nd 
        // edition, section 3.10.2.

        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

        // . Digits ExponentPart_opt FloatTypeSuffix_opt
        "(\\.("+Digits+")("+Exp+")?)|"+

        // Hexadecimal strings
        "((" +
        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
        "(0[xX]" + HexDigits + "(\\.)?)|" +

        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

        ")[pP][+-]?" + Digits + "))" +
        "[fFdD]?))" +
        "[\\x00-\\x20]*");// Optional trailing "whitespace"

    public static void logThrowable( String message , Throwable thrwbl ){
        Main.LOGGER.error( message );
        Main.LOGGER.trace( message, thrwbl );
    }
    
    public static void ShowDialogAndLogThrowable( Component parent, String message , Throwable thrwbl ) {
        logThrowable( message , thrwbl );
        
        parseExceptionMessage( parent, message, thrwbl );
    }
    
    private static void parseExceptionMessage( Component parent, String msg, Throwable thrwbl ) {
        String err = thrwbl.getMessage();
        if( err != null ) {
            if( err.contains( "Session Expired" ) ) {
                err = "Session is expired because of inactivity timeout.\nPlease log in again.";
                Main.doLogout();
            }
            else if( err.contains( "Access Denied" ) ) {
                err = "Please log in with appropriate access clearance.";
            }
            else if( err.contains( "Connection refused" ) ) {
                err = "Connection to the server is failed.\nPlease make sure client and server config is correct.";
            }
        }
        
        JOptionPane.showMessageDialog( parent,
                msg + "\n\n " + err,
                null, JOptionPane.WARNING_MESSAGE );
    }
    
    public static void attachToDesktop( javax.swing.JDesktopPane desktop , javax.swing.JInternalFrame frame , boolean isCentered ) {
        frame = (javax.swing.JInternalFrame) Main.frameMap.add( frame );
        
        boolean addToDesktop = true;
        for( Component comp : desktop.getComponents() ) {
            if( comp.equals( frame ) ) {
                addToDesktop = false;
                break;
            }
        }
        if( addToDesktop ) desktop.add( frame );
        
        if( isCentered )
            setLocationToCenter( frame, desktop );
        
        frame.pack();
        frame.setVisible( true );
        frame.moveToFront();
        frame.requestFocus();
    }
    
    public static void attachToFrame( javax.swing.JFrame parent , javax.swing.JFrame child , boolean isCentered ) {
        child = ( javax.swing.JFrame ) Main.frameMap.addAndShow( child );
        
        if( parent != null ) {
            if( isCentered )
                setLocationToCenter( child , parent );
            child.setLocationRelativeTo( parent );
        }
    }
    
    public static String nullifyEmpty( String input ) {
        if( input != null && input.trim().isEmpty() )
            return null;
        
        return input;
    }
    
    public static String removeAllNonNumber( String input ) {
        return input.replaceAll( "[^\\d]", "");
    }
    
    public static String padWithZero( int number, int length ) {
        return String.format( "%0"+length+"d", number );
    }
    
    public static boolean isFloat( String txt ) {
        return Pattern.matches( fpRegex, txt );
    }
    
    public static String padWithZero( String str, int length ) {
        return padWithZero( Integer.valueOf(str), length );
    }
    
    public static boolean isUserLogged() throws NotBoundException, MalformedURLException, RemoteException {
        // set lokasi remote object
        String url = String.format("rmi://%s:%d/%s", ClientConfig.getRmiHost(), ClientConfig.getRmiPort(), ClientConfig.RMI_USERLOG_NAME );
        
        // ambil referensi dari remote object yg ada di server
        IfAuthenticationDao    objectDao   = ( IfAuthenticationDao ) Naming.lookup(url);
        
        return objectDao.isLogged();
    }
    
    public static User getLoggedUser() throws NotBoundException, MalformedURLException, RemoteException {
        User user = null;
        
        // set lokasi remote object
        String url = String.format("rmi://%s:%d/%s", ClientConfig.getRmiHost(), ClientConfig.getRmiPort(), ClientConfig.RMI_USERLOG_NAME );
        
        // ambil referensi dari remote object yg ada di server
        IfAuthenticationDao    objectDao   = ( IfAuthenticationDao ) Naming.lookup(url);
        
        if( objectDao.isLogged() )
            user = objectDao.getUser();
        
        return user;
    }
    
    public static boolean doLogin( Component parent , String userName , String password ) throws NotBoundException, MalformedURLException, RemoteException {
        // set lokasi remote object
        String url = String.format("rmi://%s:%d/%s", ClientConfig.getRmiHost(), ClientConfig.getRmiPort(), ClientConfig.RMI_USERLOG_NAME );
        
        // ambil referensi dari remote object yg ada di server
        IfAuthenticationDao    objectDao   = ( IfAuthenticationDao ) Naming.lookup(url);

        // successfully logged?
        if( objectDao.login( userName, password ) ) {
            Main.userSession.setUser( objectDao.getUser() );
            return true;
        }
        else {
            JOptionPane.showMessageDialog( parent,
            "Couldn't log in\n\nCheck your user name and password. Check to see if Caps Lock is turned on.",
            null, JOptionPane.INFORMATION_MESSAGE );
        }
        
        return false;
    }
    
    public static void doLogout() throws NotBoundException, MalformedURLException, RemoteException {
        // log out user
        Main.userSession.removeUser();
        // set lokasi remote object
        String url = String.format("rmi://%s:%d/%s", ClientConfig.getRmiHost(), ClientConfig.getRmiPort(), ClientConfig.RMI_USERLOG_NAME );
        // ambil referensi dari remote object yg ada di server
        IfAuthenticationDao    objectDao   = ( IfAuthenticationDao ) Naming.lookup(url);
        objectDao.logout();
    }
    
    /**
     * Get current timestamp.
     * @return current timestamp.
    */
    public static Timestamp getCurrentSystemTimestamp() {
        return new Timestamp( System.currentTimeMillis() );
    }
    
    static class NoSpaceInputVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent jc) {
            return ((JTextComponent) jc).getText().indexOf( ' ') == -1;
        }
     }
}
