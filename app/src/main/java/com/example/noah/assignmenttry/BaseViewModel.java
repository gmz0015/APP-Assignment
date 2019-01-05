package com.example.noah.assignmenttry;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.noah.assignmenttry.database.ImageData;
import com.example.noah.assignmenttry.database.StartRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BaseViewModel extends AndroidViewModel {
    // Waiting for Adding
    private LiveData<List<ImageData>> mAllImage;
    private StartRepository myRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        myRepository = new StartRepository(application);
        mAllImage = myRepository.getAllImages();
    }

    // This completely hides the implementation from the UI
    LiveData<List<ImageData>> getAllImages() { return mAllImage; }

    // Create a wrapper insert() method that calls the Repository's insert() method.
    // In this way, the implementation of insert() is completely hidden from the UI.
    public void insert(ImageData imageData) { myRepository.insert(imageData); }

    public ImageData getImageByTitle(String title) { return myRepository.getImageByTitle(title); }
}
