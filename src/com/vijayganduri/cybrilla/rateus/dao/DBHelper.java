package com.vijayganduri.cybrilla.rateus.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vijayganduri.cybrilla.rateus.beans.User;
import com.vijayganduri.cybrilla.rateus.beans.Vote;


public class DBHelper extends OrmLiteSqliteOpenHelper {

	public final static String TAG = DBHelper.class.getName();

	private static String DATABASE_NAME = "cybrilla.db";
	private static int DATABASE_VERSION = 1;

	private UserDao userDao = null;
	private VoteDao voteDao = null;

	public DBHelper( Context context ) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}

	@Override
	public void onCreate( SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource ){
		try {
			TableUtils.createTable( connectionSource, User.class );
			TableUtils.createTable( connectionSource, Vote.class );
		} catch( SQLException e ) {
			Log.e( TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade( SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion ){
		try {
			TableUtils.dropTable(connectionSource, User.class, true );
			TableUtils.dropTable(connectionSource, Vote.class, true );
			onCreate( sqLiteDatabase, connectionSource );
		} catch (SQLException e) {
			Log.e( TAG, "Can't upgrade databases", e );
			throw new RuntimeException(e);
		}
	}

	public UserDao getUserDao(){
		if (userDao == null) {
			try{
				userDao = (UserDao)DaoManager.createDao( getConnectionSource(), User.class );
			}catch( SQLException e ){
				Log.e( TAG, "Can't get user dao", e );
				throw new RuntimeException(e);
			}
		}
		return userDao;
	}

	public VoteDao getVoteDao(){
		if (voteDao == null) {
			try{
				voteDao = (VoteDao)DaoManager.createDao( getConnectionSource(), Vote.class );
			}catch( SQLException e ){
				Log.e( TAG, "Can't get vote dao", e );
				throw new RuntimeException(e);
			}
		}
		return voteDao;
	}

	public void clearAllUsers(){
		try{
			TableUtils.clearTable(getConnectionSource(), User.class);
		}catch( SQLException e ){
			Log.e( TAG, "Can't clear table user", e );
			throw new RuntimeException(e);
		}
	}

	public void clearAllVotes(){
		try{
			TableUtils.clearTable(getConnectionSource(), User.class);
		}catch( SQLException e ){
			Log.e( TAG, "Can't clear table vote", e );
			throw new RuntimeException(e);
		}
	}   
	@Override
	public void close() {
		super.close();
		userDao = null;
		voteDao = null;
	}

}
