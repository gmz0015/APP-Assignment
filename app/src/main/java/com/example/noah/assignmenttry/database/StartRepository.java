package com.example.noah.assignmenttry.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StartRepository {
    private final MyDAO myDAO;
    private LiveData<List<ImageData>> myAllImage;

    public StartRepository(Application application) {
        MyDatabase db = MyDatabase.getDatabase(application);
        myDAO = db.myDao();
        myAllImage = myDAO.getAllImages();
    }

    public LiveData<List<ImageData>> getAllImages() {
        return myAllImage;
    }


    public void insert (ImageData image) {
        new insertAsyncTask(myDAO).execute(image);
    }

    private static class insertAsyncTask extends AsyncTask<ImageData, Void, Void> {

        private MyDAO mAsyncTaskDao;

        insertAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ImageData... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public ImageData getImageByTitle(String title)  {
        ImageData imageData = null;
        try {
            imageData = new getAsyncTask(myDAO).execute(title).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return imageData;
    }

    private static class getAsyncTask extends AsyncTask<String, Void, ImageData> {

        private MyDAO mAsyncTaskDao;

        getAsyncTask(MyDAO dao) { mAsyncTaskDao = dao; }

        @Override
        protected ImageData doInBackground(final String... params) {
            return mAsyncTaskDao.getImageByTitle(params[0]);
        }
    }

}