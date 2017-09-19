package com.myapp.newapp.api.model;

/**
 * Created by ishan on 19-09-2017.
 */

public class Publisher {
    private String Id;
    private String Name;
    private String Image;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
