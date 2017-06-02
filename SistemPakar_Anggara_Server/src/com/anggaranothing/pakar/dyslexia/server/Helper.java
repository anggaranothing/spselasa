/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.server;

/**
 *
 * @author AnggaraNothing
 */
public class Helper extends com.anggaranothing.pakar.dyslexia.Helper {
    
    public static void logThrowable( String message , Throwable thrwbl ){
        Main.LOGGER.error( message );
        Main.LOGGER.trace( message, thrwbl );
    }
    
}
