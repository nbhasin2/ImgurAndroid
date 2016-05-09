package epicara.younility.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nishant on 16-05-08.
 */
public class Image implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("datetime")
    private Integer datetime;

    @SerializedName("link")
    private String link;

    @SerializedName("animated")
    private boolean animated;

    @SerializedName("width")
    private Integer width;

    @SerializedName("height")
    private Integer height;

    @SerializedName("size")
    private Integer size;

    @SerializedName("views")
    private Integer views;

    @SerializedName("gifv")
    private String gifv;

    @SerializedName("mp4")
    private String mp4;

    @SerializedName("webm")
    private String webm;

    @SerializedName("section")
    private String section;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }


    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        bundle.putString("Link", link);
        bundle.putString("Title", title);
        bundle.putInt("Views", views);

        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Creator<Image>() {

        @Override
        public Image createFromParcel(Parcel source) {
            // read the bundle containing key value pairs from the parcel
            Bundle bundle = source.readBundle();

            // instantiate a person using values from the bundle
            return new Image();
        }
        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }

    };
}
