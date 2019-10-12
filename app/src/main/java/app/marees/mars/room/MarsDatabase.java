package app.marees.mars.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import app.marees.mars.Singletons.Myapp;

@Database(entities = MyEntity.class, version = 1)
public abstract class MarsDatabase extends RoomDatabase {
    private static MarsDatabase myInstance;
    private static RoomDatabase.Callback myCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopuplateMyDbAsyncTask(myInstance).execute();
        }
    };

    public static synchronized MarsDatabase getInstance(Context myCtx) {

        if (myInstance == null) {

            myInstance = Room.databaseBuilder(Myapp.getAppContext(), MarsDatabase.class, "mars_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(myCallback)
                    .build();

        }
        return myInstance;

    }

    public abstract DAO marsdao();

    private static class PopuplateMyDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private DAO marsdao;

        private PopuplateMyDbAsyncTask(MarsDatabase db) {
            marsdao = db.marsdao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            marsdao.insert(new MyEntity("test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test"));

            return null;
        }
    }

}
