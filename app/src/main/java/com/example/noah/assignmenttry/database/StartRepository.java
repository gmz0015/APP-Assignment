package com.example.noah.assignmenttry.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StartRepository {
    private final MyDAO myDAO;
    private LiveData<List<ImageData>> myAllImage;
    private LiveData<List<ImageData>> mImageByTitle;

    public StartRepository(Application application) {
        MyDatabase db = MyDatabase.getDatabase(application);
        myDAO = db.myDao();
        myAllImage = myDAO.getAllImages();
    }


    /**
     * Get all images
     *
     * @return a LiveData List
     */
    public LiveData<List<ImageData>> getAllImages() {
        return myAllImage;
    }



    /**
     * Get the images containing the words in title
     *
     * @param word the search string
     * @return the image which contains the word
     */
    public LiveData<List<ImageData>> getImageByWord(String word)  {
        mImageByTitle = myDAO.getImageByWord(word);
        return mImageByTitle;
    }

    /**
     * Get the image by title
     *
     * @param title the title displaying on map
     * @return the image which has the same title
     */
    public ImageData getImageByTitle(String title)  {
        ImageData imageData = null;
        try {
            imageData = new getAsyncTask(myDAO).execute(title).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return imageData;
    }

    /**
     * Insert the new image to database
     * @param image the ImageData
     */
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



    private static class getAsyncTask extends AsyncTask<String, Void, ImageData> {

        private MyDAO mAsyncTaskDao;

        getAsyncTask(MyDAO dao) { mAsyncTaskDao = dao; }

        @Override
        protected ImageData doInBackground(final String... params) {
            return mAsyncTaskDao.getImageByTitle(params[0]);
        }
    }

}