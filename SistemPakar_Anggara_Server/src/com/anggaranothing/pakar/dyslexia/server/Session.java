/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.server;

import com.anggaranothing.pakar.dyslexia.model.User;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AnggaraNothing
 */
public class Session {
    private final Map<String,User>      loggedUser      = new HashMap<String,User>();
    private final Map<String,Timestamp> lastActiveUser  = new HashMap<String,Timestamp>();
    
    /**
     * Get current timestamp.
     * @return current timestamp.
    */
    private static Timestamp getCurrentSystemTimestamp() {
        return new Timestamp( System.currentTimeMillis() );
    }
    
    public boolean isUserLogged( String client_ip ) {
        return loggedUser.containsKey( client_ip );
    }
    
    public boolean isUserLogged( User userObj ) {
        return loggedUser.containsValue( userObj );
    }
    
    public boolean isSessionExpired( String client_ip ) {
        Timestamp lastActive = getUserLastActive( client_ip );
        if( lastActive != null ) {
            int timeoutSec = ServerConfig.getClientSessionTimeout();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            
            lastActive = new Timestamp(lastActive.getTime() + (timeoutSec * 1000L));
            
            if( !currentTime.after( lastActive ) ) {
                addUserLastActive(client_ip, getCurrentSystemTimestamp() );
                return false;
            }
        }
        return true;
    }
    
    public Timestamp getUserLastActive( String client_ip ) {
        return lastActiveUser.get( client_ip );
    }
    
    public void addUserLastActive( String userIp , Timestamp timestamp ) {
        lastActiveUser.put( userIp, timestamp );
    }
    
    public boolean removeUserLastActive( String userIp ) {
        return lastActiveUser.remove( userIp ) != null;
    }
    
    public User getLoggedUser( String client_ip ) {
        return loggedUser.get( client_ip );
    }
    
    public void addLoggedUser( String userIp , User model ) {
        if( isUserLogged( model ) ) {
            // find active client ip with this user data
            for( Map.Entry<String,User> entry : loggedUser.entrySet() ) {
                if( entry.getValue().getId() == model.getId() ){
                    // force log out
                    removeLoggedUser( entry.getKey() );
                    break;
                }
            }
        }
        addUserLastActive( userIp, getCurrentSystemTimestamp() );
        loggedUser.put( userIp, model );
    }
    
    public boolean removeLoggedUser( String userIp ) {
        removeUserLastActive( userIp );
        return loggedUser.remove( userIp ) != null;
    }
}
