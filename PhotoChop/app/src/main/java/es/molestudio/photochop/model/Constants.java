package es.molestudio.photochop.model;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Chus on 23/02/15.
 */
public class Constants {

    public static final String TAG_DEBUG = "PhotoChop.DEBUG";
    public static int MAX_LOCATION_INTENTS = 3;

    public static final Integer THUMBNAIL_MAP_WIDTH = 60;
    public static final Integer THUMBNAIL_MAP_HEIGTH = 60;


    public static final Integer THUMBNAIL_DEFAULT_WIDTH = 85;
    public static final Integer THUMBNAIL_DEFAULT_HEIGTH = 85;


    public static DisplayImageOptions UIL_DISPLAY_IMAGE_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(null)
            .showImageForEmptyUri(null)
            .showImageOnFail(null)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

}
