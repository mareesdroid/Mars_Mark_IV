package app.marees.mars.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DAO {


    //    LiveData<List<MyEntity>> getAllData();
//
    @Insert
    void insert(MyEntity myEntity);

    //
    @Update
    void update(MyEntity myEntity);

    //
    @Delete
    void delete(MyEntity myEntity);

    @Query("DELETE FROM marees_mark_iv")
    void deleteAllDta();


    @Query("SELECT * FROM marees_mark_iv WHERE date = :myID")
    Cursor getDataDate(String myID);

    @Query("UPDATE marees_mark_iv SET isSms = :status WHERE date = :myDate")
    void setSmsStatus(String status, String myDate);

    @Query("UPDATE marees_mark_iv SET isCont = :status WHERE date = :date")
    void setContactStatus(String status, String date);

    @Query("UPDATE marees_mark_iv SET isCall = :status WHERE date = :date")
    void setCallStatus(String status, String date);
}
