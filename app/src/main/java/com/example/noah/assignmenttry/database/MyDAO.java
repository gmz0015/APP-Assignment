package com.example.noah.assignmenttry.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyDAO {
    @Query("SELECT * FROM image_table")
    LiveData<ImageData> getAll();

    @Query("SELECT * FROM image_table WHERE imagePath IN (:imagePaths)")
    LiveData<ImageData> loadAllByIds(String[] imagePaths);

    @Query("SELECT * FROM image_table WHERE title LIKE :word")
    LiveData<List<ImageData>> getImageByWord(String word);

    @Query("SELECT * FROM image_table WHERE time LIKE :imageDate")
    List<ImageData> searchImageByWordInDate(String imageDate);

    @Query("SELECT * FROM image_table WHERE title LIKE :imageTitle")
    List<ImageData> searchImageByWordInTitle(String imageTitle);

    @Query("SELECT * FROM image_table WHERE description LIKE :imageDescription")
    List<ImageData> searchImageByWordInDescription(String imageDescription);

    @Query("SELECT * FROM image_table WHERE title = :imageTitle")
    ImageData getImageByTitle(String imageTitle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ImageData imageData);

    @Query("SELECT * from image_table ORDER BY imagePath ASC")
    LiveData<List<ImageData>> getAllImages();

    @Delete
    void delete(ImageData imageData);

    @Query("DELETE FROM image_table")
    void deleteAll();
}
