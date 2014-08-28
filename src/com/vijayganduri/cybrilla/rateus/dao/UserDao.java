package com.vijayganduri.cybrilla.rateus.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.vijayganduri.cybrilla.rateus.beans.User;

public class UserDao extends BaseDaoImpl<User,String> {

    public static final String TAG = UserDao.class.toString();

    public UserDao( ConnectionSource connectionSource, Class<User> dataClass ) throws SQLException{
        super( connectionSource, dataClass );
    }

    public UserDao( ConnectionSource connectionSource, DatabaseTableConfig<User> tableConfig ) throws SQLException{
        super( connectionSource, tableConfig );
    }

    public UserDao( Class<User> dataClass ) throws SQLException{
        super( dataClass );
    }

    public CreateOrUpdateStatus createOrUpdate( User user) {
		CreateOrUpdateStatus result = null;
        try{
            result = super.createOrUpdate( user );
        }catch( SQLException e ){
            e.printStackTrace();
        }
        return result;
    }
    
    public User getUserById(String id) {
        User user = null;
        try{
        	user = queryForId(id);
        }catch( SQLException e ){
            e.printStackTrace();
        }
        return user;
    }
    
    public List<User> getAllUsers() {
        List<User> users = null;
        try{
        	users = queryForAll();
        }catch( SQLException e ){
            e.printStackTrace();
        }
        return users;
    }

    public void removeUser(User user) {
        try{
        	delete(user);
        }catch( SQLException e ){
            e.printStackTrace();
        }
    }
    
    public void removeUserById(String id) {
        try{
        	deleteById(id);
        }catch( SQLException e ){
            e.printStackTrace();
        }
    }
    
}