package app.marees.mars.room;

import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.marees.mars.Singletons.Myapp;

public class DataSetter implements DataTracker {

    private static MyViewModel marsDb;

    @Override
    public void getTodayData(Cursor data) {
        Log.e("Database", "Checked.....");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        marsDb = Myapp.getMyViewModel();
        Date date = new Date();
        String todayDate = dateFormat.format(date);

        marsDb.insert(new MyEntity("test", "test", "test", "test", "test", "test", todayDate, "test", "test", "test", "no", "no", "no"));
        if (data.getCount() == 0) {
            Log.e("Database", "Data not exist");
            Myapp.setisSent(false);
        } else {
            Log.e("Database", "Data exist skipping get data avoiding more data");
            Myapp.setisSent(true);

        }
    }
}
