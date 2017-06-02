/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author AnggaraNothing
 */
public abstract class Helper {
    
    public static final int GROUP_ALL     = 0;
    
    /**
     * Set component location to the center of the screen.
     * @param comp the Component to set
     */
    public static void setLocationToCenter( Component comp ) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int width  = dim.width / 2  - comp.getSize().width / 2,
            height = dim.height / 2 - comp.getSize().height / 2;
        comp.setLocation( width, height );
    }
    
    /**
     * Set component location to the center of the component destination.
     * @param compSource the source to set at destination
     * @param compDestination the destination
     */
    public static void setLocationToCenter( Component compSource , Component compDestination ) {
        Dimension destinationSize   = compDestination.getSize();
        Dimension sourceSize        = compSource.getSize();
        
        int width  = ( destinationSize.width  - sourceSize.width )  / 2,
            height = ( destinationSize.height - sourceSize.height ) / 2;
        
        compSource.setLocation( width, height );
    }
    
    public static double getScaleFactor(int iMasterSize, int iTargetSize) {

        double dScale = 1;
        if (iMasterSize > iTargetSize) {

            dScale = (double) iTargetSize / (double) iMasterSize;

        } else {

            dScale = (double) iTargetSize / (double) iMasterSize;

        }

        return dScale;

    }

    public static double getScaleFactorToFit(Dimension original, Dimension toFit) {

        double dScale = 1d;

        if (original != null && toFit != null) {

            double dScaleWidth = getScaleFactor(original.width, toFit.width);
            double dScaleHeight = getScaleFactor(original.height, toFit.height);

            dScale = Math.min(dScaleHeight, dScaleWidth);

        }

        return dScale;

    }
    
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        if( email == null )
            return false;
        try {
           InternetAddress emailAddr = new InternetAddress(email);
           emailAddr.validate();
        } catch (AddressException ex) {
           result = false;
        }
        return result;
    }
}
