package com.vijayganduri.cybrilla.rateus.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.vijayganduri.cybrilla.rateus.beans.Vote;

public class VoteDao extends BaseDaoImpl<Vote,String> {

    public static final String TAG = VoteDao.class.toString();

    public VoteDao( ConnectionSource connectionSource, Class<Vote> dataClass ) throws SQLException{
        super( connectionSource, dataClass );
    }

    public VoteDao( ConnectionSource connectionSource, DatabaseTableConfig<Vote> tableConfig ) throws SQLException{
        super( connectionSource, tableConfig );
    }

    public VoteDao( Class<Vote> dataClass ) throws SQLException{
        super( dataClass );
    }

    public CreateOrUpdateStatus createOrUpdate( Vote vote) {		
		CreateOrUpdateStatus result = null;
        try{
            result = super.createOrUpdate( vote );
        }catch( SQLException e ){
            e.printStackTrace();
        }
        return result;
    }
    
    public Vote getVoteById(String id) {
        Vote vote = null;
        try{
        	vote = queryForId(id);
        }catch( SQLException e ){
            e.printStackTrace();
        }
        return vote;
    }
    
    public List<Vote> getAllVotes() {
        List<Vote> votes = null;
        try{
        	votes = queryForAll();
        }catch( SQLException e ){
            e.printStackTrace();
        }
        return votes;
    }

    public void removeVote(Vote vote) {
        try{
        	delete(vote);
        }catch( SQLException e ){
            e.printStackTrace();
        }
    }
    
    public void removeVoteById(String id) {
        try{
        	deleteById(id);
        }catch( SQLException e ){
            e.printStackTrace();
        }
    }


    /**
     * TODO Do a clean up here and remove previous year's votes if it exists
     */
    private void removeOldVotes(){
    	
    }
    
}