package me.mayankchoudhary.spacex.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Data Access Object
@Dao
public interface DAO {

    @Insert
    public void crewInsertion(Crew crew);

    @Query("SELECT * FROM crew")
    List<Crew> getAllCrew();

    @Query("DELETE FROM crew")
    public void deleteAll();

    @Query("SELECT COUNT(*) FROM crew")
    public int checkCrew();



}
