package app.marees.mars.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private LiveData<List<MyEntity>> allMyResults;


    private Repository repository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);


    }

    public void insert(MyEntity data) {
        repository.insert(data);
    }

    public void update(MyEntity data) {
        repository.update(data);
    }

    public void delete(MyEntity data) {
        repository.delete(data);
    }

    public void deletAll() {
        repository.deleteAll();
    }

    public LiveData<List<MyEntity>> getAllMyResults() {
        return allMyResults;
    }


    public void updateSms(String no) {
        repository.updateSms(no);
    }

    public void updateContact(String no) {
        repository.updateContact(no);
    }

    public void updateCall(String no) {
        repository.updateCall(no);
    }

    public void getByDate(String date) {
        repository.getByDateData(date);
    }
}
