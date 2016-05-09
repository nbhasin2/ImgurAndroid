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

    public ImageModel(String name, String url, String description ) {
        mName = name;
        mUrl = url;
        mDescription = description;
    }

    // Getters and Setters

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public List<Image> getImages() {
        return mImages;
    }
}
