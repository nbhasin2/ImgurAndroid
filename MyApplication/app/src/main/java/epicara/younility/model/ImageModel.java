package epicara.younility.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nishant on 16-05-07.
 */
public class ImageModel {
    private String mName;
    private String mUrl; // Image url
    private String mDescription; // Image Description

    @SerializedName("data")
    private List<Image> mImages;

    public ImageModel(List<Image> imageList)
    {
        mImages = imageList;
    }


    // Getters and Setters

    public List<Image> getImages() {
        return mImages;
    }
}
