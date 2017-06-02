/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.impl;

import com.anggaranothing.pakar.dyslexia.dao.IfGroupDao;
import com.anggaranothing.pakar.dyslexia.model.Group;
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
public class GroupDao extends DaoFactory implements IfGroupDao {

    public GroupDao(Connection conn) throws RemoteException {
        super(conn);
        setAccessPrivilege( ServerConfig.getPrivilegeGroup() );
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
    public int create(Group groupObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " GroupDao create() accessed !" );

        setSqlQuery("INSERT INTO data_user_grup " +
                    "(grup_name) " +
                    "VALUES (?)");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString( 1, groupObj.getName() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "GroupDao create() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public List<Group> read() throws RemoteException {
        defaultAccessChecking();
        
        List<Group> objList = new ArrayList<Group>();

        Main.LOGGER.info( "["+getClientIp()+"]" + " GroupDao read() accessed !" );

        setSqlQuery("SELECT grup_id , grup_name " +
                    "FROM data_user_grup " +
                    "ORDER BY grup_id");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Group obj = new Group();
                obj.setId(        rs.getInt(       "grup_id" ) );
                obj.setName(      rs.getString(    "grup_name" ) );

                // simpan objek barang ke dalam objek class List
                objList.add( obj );
            }
        } catch (SQLException ex) {
            Main.LOGGER.error( "GroupDao read() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return objList;
    }

    @Override
    public int update(Group groupObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " GroupDao update() accessed !" );

        setSqlQuery("UPDATE data_user_grup SET"
                + " grup_name = ?"
                + " WHERE grup_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setString(    1, groupObj.getName() );
            ps.setInt(       2, groupObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "GroupDao update() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }

    @Override
    public int delete(Group groupObj) throws RemoteException {
        defaultAccessChecking();
        
        int result = 0;

        Main.LOGGER.info( "["+getClientIp()+"]" + " GroupDao delete() accessed !" );

        setSqlQuery("DELETE FROM data_user_grup " +
                    "WHERE grup_id = ?");
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = getConn().prepareStatement(getSqlQuery());

            // set nilai parameter yg akan dikirim ke sql
            ps.setInt( 1, groupObj.getId() );

            // jalankan perintah sql
            result = ps.executeUpdate();

        } catch (SQLException ex) {
            Main.LOGGER.error( "GroupDao delete() is failed !", ex );
            throw new RemoteException( ex.getMessage() );
        }

        return result;
    }
    
}
