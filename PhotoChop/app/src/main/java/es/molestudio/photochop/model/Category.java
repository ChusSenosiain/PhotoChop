package es.molestudio.photochop.model;

import java.io.Serializable;

/**
 * Created by Chus on 31/12/14.
 */
public class Category extends Selectable implements Serializable {


    public Category(int cagetoryId, String categoryName, String categoryDescription) {
        super(cagetoryId, categoryName, categoryDescription);
    }

    public Category() {
        super();
    }

}
