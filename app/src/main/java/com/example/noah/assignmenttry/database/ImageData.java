package com.example.noah.assignmenttry.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.File;


@Entity(tableName = "image_table")
public class ImageData {
    @PrimaryKey
    @NonNull
    private String imagePath;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "latitude")
    private Double latitide;

    @ColumnInfo(name = "time")
    private String time;

    public ImageData (String imagePath, String title, String description, Double longitude, Double latitide, String time) {
        this.imagePath = imagePath;
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitide = latitide;
        this.time = time;
    }

    public String getImagePath() { return this.imagePath; }

    public String getTitle() { return this.title; }

    public String getDescription() { return this.description; }

    public Double getLongitude() { return  this.longitude; }

    public Double getLatitide() { return  this.latitide; }

    public String getTime() { return this.time; }
}
