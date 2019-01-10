package com.example.noah.assignmenttry;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.noah.assignmenttry.database.ImageData;
import com.example.noah.assignmenttry.database.StartRepository;

import java.util.List;

public class BaseViewModel extends AndroidViewModel {
//    private LiveData<List<ImageData>> mImageByWord;// TODO Waiting to delete
    private LiveData<List<ImageData>> mAllImage;
    private StartRepository myRepository;
//    private LiveData<List<ImageData>> mImage;// TODO Waiting to delete
    private final MutableLiveData<Integer> imageLiveDataTrigger;
    private final MutableLiveData<String> searchLiveDataTrigger;
    public final LiveData<List<ImageData>> imageDataLiveData;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        myRepository = new StartRepository(application);


        mAllImage = myRepository.getAllImages();

        mAllImage.getValue();

        // Trigger
        imageLiveDataTrigger = new MutableLiveData<>();
        searchLiveDataTrigger = new MutableLiveData<>();

        ImageLiveDataFilter imageLiveDataFilter = new ImageLiveDataFilter(imageLiveDataTrigger, searchLiveDataTrigger);

        imageDataLiveData = Transformations.switchMap(imageLiveDataFilter, input -> input);
    }


    /**
     * This completely hides the implementation from the UI
     *
     * @return all images
     */
    LiveData<List<ImageData>> getAllImages() { return mAllImage; }



    // TODO Waiting to Delete
    /**
     * Get the images containing the word in title
     *
     * @param
     * @return
     */
//    LiveData<List<ImageData>>  getImageByWord(String word) {
//        mImageByWord = myRepository.getImageByWord(word);
//        return mImageByWord;
//    }

    public LiveData<List<ImageData>> getImageDataLiveData(){ return imageDataLiveData; }



    public void setImageDataTrigger(int STATE) {
        // Set trigger
        imageLiveDataTrigger.setValue(STATE);
    }

    public void setSearchTrigger(String word) {
        searchLiveDataTrigger.setValue(word);
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



    public void delete(ImageData imageData) { myRepository.delete(imageData); }

    class ImageLiveDataFilter extends MediatorLiveData<LiveData<List<ImageData>>> {

        public ImageLiveDataFilter(LiveData<Integer> imageTrigger, LiveData<String> word) {

            addSource(imageTrigger, state -> {
                if (state == 0) {

                    // Observe all images
                    setValue(myRepository.getAllImages());
                } else {

                    // Observe searched image
                    setValue(myRepository.getImageByWord(""));
                }
            });

            addSource(word, w -> {

                // Observe searched image
                setValue(myRepository.getImageByWord(w));
            });
        }
    }
}
