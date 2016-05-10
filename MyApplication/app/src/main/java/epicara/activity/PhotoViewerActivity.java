package epicara.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.doctoror.gifimageloader.DefaultGifImageLoader;
import com.doctoror.gifimageloader.GifImageLoader;
import com.doctoror.gifimageloader.NetworkGifImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import epicara.Global.Constants;
import epicara.activity.R;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewerActivity extends AppCompatActivity {

    private String mImageUrl = "";
    private NetworkGifImageView mImageView;
    private ImageView mImageViewRegular;
    private PhotoViewAttacher mAttacher;
    private Boolean isGif = false;
    private GifImageLoader mImageLoader;

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
        mImageLoader = DefaultGifImageLoader
                .getInstance(this, epicara.Global.Configuration.LRU_CACHE_SIZE_IN_BYTES);
                
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mImageUrl = extras.getString(Constants.IMAGE_LINK,"");
            isGif = extras.getBoolean(Constants.IMAGE_IS_GIF, false);
        }
    }

    public void setupPhotoView()
    {
        mImageView = (NetworkGifImageView) findViewById(R.id.photo_img);
        mImageViewRegular = (ImageView) findViewById(R.id.photo_img_regular);
        mImageView.setVisibility(View.GONE);
        mImageViewRegular.setVisibility(View.GONE);

        if(!mImageUrl.isEmpty()) {
            // Any implementation of ImageView can be used!
            if(isGif)
            {
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageUrl(mImageUrl, mImageLoader);
            }
            else {
                mImageViewRegular.setVisibility(View.VISIBLE);
                ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
                imageLoader.loadImage(mImageUrl, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap

                        mImageViewRegular.setImageBitmap(loadedImage);
                        mAttacher = new PhotoViewAttacher(mImageViewRegular);
                    }
                });
            }

        }
    }

}
