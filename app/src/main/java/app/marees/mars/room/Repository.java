package app.marees.mars.room;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class Repository {

    private DAO marsDao;

    public Repository(Application application) {
        MarsDatabase myDb = MarsDatabase.getInstance(application);
        marsDao = myDb.marsdao();


    }


    public void getByDateData(String date) {
        new DateDataAsynctask(date, marsDao).execute();

    }

    public void insert(MyEntity data) {
        new InsertDataAsynctask(marsDao).execute(data);
    }

    public void update(MyEntity data) {
        new UpdateDataAsynctask(marsDao).execute(data);
    }

    public void delete(MyEntity data) {
        new DeleteDataAsynctask(marsDao).execute(data);
    }

    public void deleteAll() {
        new DeleteAllAsynctask(marsDao).execute();
    }


    public void updateSms(String no) {
        new UpdateSmsAsynctask(no, marsDao).execute();
    }

    public void updateContact(String no) {
        new UpdateContactAsynctask(no, marsDao).execute();
    }

    public void updateCall(String no) {

        new UpdateCallAsynctask(no, marsDao).execute();
    }

    private static class UpdateCallAsynctask extends AsyncTask<Void, Void, Void> {
        DAO mars;
        String status;
        String date;

        public UpdateCallAsynctask(String status, DAO marsDao) {
            this.mars = marsDao;
            this.status = status;
            this.date = date;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mars.setCallStatus(status, date);
            return null;
        }
    }


    private static class UpdateContactAsynctask extends AsyncTask<Void, Void, Void> {
        DAO mars;
        String status;
        String date;

        public UpdateContactAsynctask(String status, DAO marsDao) {
            this.mars = marsDao;
            this.status = status;
            this.date = date;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mars.setContactStatus(status, date);
            return null;
        }
    }


    private static class InsertDataAsynctask extends AsyncTask<MyEntity, Void, Void> {

        private DAO marsdao;

        private InsertDataAsynctask(DAO marsdao) {
            this.marsdao = marsdao;
        }

        @Override
        protected Void doInBackground(MyEntity... myEntities) {
            marsdao.insert(myEntities[0]);
            return null;
        }
    }

    private static class UpdateDataAsynctask extends AsyncTask<MyEntity, Void, Void> {

        private DAO marsdao;

        private UpdateDataAsynctask(DAO marsdao) {
            this.marsdao = marsdao;
        }

        @Override
        protected Void doInBackground(MyEntity... myEntities) {
            marsdao.update(myEntities[0]);
            return null;
        }
    }

    private static class DeleteDataAsynctask extends AsyncTask<MyEntity, Void, Void> {

        private DAO marsdao;

        private DeleteDataAsynctask(DAO marsdao) {
            this.marsdao = marsdao;
        }

        @Override
        protected Void doInBackground(MyEntity... myEntities) {
            marsdao.delete(myEntities[0]);
            return null;
        }
    }

    private static class DeleteAllAsynctask extends AsyncTask<Void, Void, Void> {

        private DAO marsdao;

        private DeleteAllAsynctask(DAO marsdao) {
            this.marsdao = marsdao;
        }

        @Override
        protected Void doInBackground(Void... Void) {
            marsdao.deleteAllDta();
            return null;
        }
    }

    private static class DateDataAsynctask extends AsyncTask<Void, Void, Cursor> {
        DAO mars;
        String date;

        public DateDataAsynctask(String date, DAO marsDao) {
            this.mars = marsDao;
            this.date = date;
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Log.e("Database", "Checking if data is exist for today...");
            ///start task
            return mars.getDataDate(date);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            Log.e("Database", "Checking Completed");
            //here when task finished
            DataSetter m = new DataSetter();
            if (cursor != null) {
                m.getTodayData(cursor);
            } else {
                m.getTodayData(cursor);
            }
        }
    }

    private static class UpdateSmsAsynctask extends AsyncTask<Void, Void, Void> {
        DAO mars;
        String status;
        String date;

        public UpdateSmsAsynctask(String status, DAO marsDao) {
            this.mars = marsDao;
            this.status = status;
            this.date = date;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mars.setSmsStatus(status, date);
            return null;
        }
    }
}
