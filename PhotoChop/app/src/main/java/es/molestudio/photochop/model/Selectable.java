package es.molestudio.photochop.model;

import java.io.Serializable;

/**
 * Created by Chus on 02/03/15.
 */
public class Selectable implements Serializable{

    private Integer mId;
    private String mName;
    private String mDescription;


    public Selectable(Integer id, String name, String description) {
        mId = id;
        mName = name;
        mDescription = description;
    }

    public Selectable() {
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
