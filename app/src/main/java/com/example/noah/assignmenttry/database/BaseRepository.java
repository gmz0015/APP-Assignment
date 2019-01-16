package com.example.noah.assignmenttry.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BaseRepository {
    private final MyDAO myDAO;
    private LiveData<List<ImageData>> myAllImage;
    private LiveData<List<ImageData>> mImageByWord;

    public BaseRepository(Application application) {
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
        String searchWord = "%" + word + "%";
        mImageByWord = myDAO.getImageByWord(searchWord);
        return mImageByWord;
    }




    /**
     * Get the image by title
     *
     * @param title the title displaying on map
     * @return the image which has the same title
     */
    public List<ImageData> searchImageByWordAll(String date, String title, String description)  {
        List<ImageData> imageData = new ArrayList<>();
        try {
            imageData = new searchAsyncTask(myDAO).execute(date, title, description).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return imageData;
    }


    private static class searchAsyncTask extends AsyncTask<String, Void, List<ImageData>> {

        private MyDAO mAsyncTaskDao;

        searchAsyncTask(MyDAO dao) { mAsyncTaskDao = dao; }

        @Override
        protected List<ImageData> doInBackground(final String... params) {
            List<ImageData> imageData = new ArrayList<>();
            imageData.addAll(mAsyncTaskDao.searchImageByWordInDate("%" + params[0] + "%"));
            imageData.addAll(mAsyncTaskDao.searchImageByWordInTitle("%" + params[1] + "%"));
            imageData.addAll(mAsyncTaskDao.searchImageByWordInDescription("%" + params[2] + "%"));
            return imageData;
        }
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


    private static class getAsyncTask extends AsyncTask<String, Void, ImageData> {

        private MyDAO mAsyncTaskDao;

        getAsyncTask(MyDAO dao) { mAsyncTaskDao = dao; }

        @Override
        protected ImageData doInBackground(final String... params) {
            return mAsyncTaskDao.getImageByTitle(params[0]);
        }
    }




    /**
     * Insert the new image to database
     * @param image the ImageData
     */
    public void delete (ImageData image) {
        new deleteAsyncTask(myDAO).execute(image);
    }


    private static class deleteAsyncTask extends AsyncTask<ImageData, Void, Void> {

        private MyDAO mAsyncTaskDao;

        deleteAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ImageData... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}