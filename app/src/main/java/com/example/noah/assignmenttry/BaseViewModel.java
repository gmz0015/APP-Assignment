package com.example.noah.assignmenttry;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.noah.assignmenttry.database.ImageData;
import com.example.noah.assignmenttry.database.StartRepository;

import java.util.List;

public class BaseViewModel extends AndroidViewModel {
    private LiveData<List<ImageData>> mImageByWord;
    private LiveData<List<ImageData>> mAllImage;
    private StartRepository myRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        myRepository = new StartRepository(application);
        mAllImage = myRepository.getAllImages();
    }


    /**
     * This completely hides the implementation from the UI
     *
     * @return all images
     */
    LiveData<List<ImageData>> getAllImages() { return mAllImage; }



    /**
     * Get the images containing the word in title
     *
     * @param word
     * @return
     */
    LiveData<List<ImageData>>  getImageByWord(String word) {
        mImageByWord = myRepository.getImageByWord(word);
        return mImageByWord;
    }



    /**
     * Get the images by title
     *
     * Invoke by MapsFragment
     *
     * @param title
     * @return
     */
    public ImageData getImageByTitle(String title) { return myRepository.getImageByTitle(title); }



    /**
     * Create a wrapper insert() method that calls the Repository's insert() method.
     * In this way, the implementation of insert() is completely hidden from the UI.
     *
     * @param imageData
     */
    public void insert(ImageData imageData) { myRepository.insert(imageData); }
}
