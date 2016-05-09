package epicara.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import epicara.Global.Constants;
import epicara.activity.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewerActivity extends AppCompatActivity {

    private String mImageUrl = "";
    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setImageData();
        setupPhotoView();

    }

    public void setImageData()
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString(Constants.IMAGE_LINK);
            mImageUrl = value;
        }
    }

    public void setupPhotoView()
    {
        if(!mImageUrl.isEmpty()) {
            // Any implementation of ImageView can be used!
            mImageView = (ImageView) findViewById(R.id.photo_img);

            ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
            imageLoader.loadImage(mImageUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // Do whatever you want with Bitmap

                    mImageView.setImageBitmap(loadedImage);
                    mAttacher = new PhotoViewAttacher(mImageView);
                }
            });

        }
    }

}
