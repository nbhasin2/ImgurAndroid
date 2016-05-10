package epicara.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.doctoror.gifimageloader.DefaultGifImageLoader;
import com.doctoror.gifimageloader.GifImageLoader;
import com.doctoror.gifimageloader.NetworkGifImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sefford.circularprogressdrawable.CircularProgressDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import epicara.UI.CardImageView;
import epicara.activity.R;
import epicara.younility.model.ImageModel;
import it.gmariotti.cardslib.library.internal.Card;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by nishant on 16-05-08.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
{
    // Variables

    private ArrayList<epicara.younility.model.Image> mImageModelList;
    private CircularProgressDrawable circularLoader;
    private GifImageLoader mImageLoader;
    // Constructor

    public ImageAdapter(ArrayList<epicara.younility.model.Image> imageModellist, Context context)
    {
        mImageModelList = imageModellist;

        mImageLoader = DefaultGifImageLoader
                .getInstance(context, epicara.Global.Configuration.LRU_CACHE_SIZE_IN_BYTES);

        ContextCompat.getColor(context, R.color.colorGreen);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView imageDescription;
        public CardImageView imageItem;
        public GifImageView progressGif;
        public NetworkGifImageView imageIteamNetwork;

        public ViewHolder(View itemView) {

            super(itemView);

            imageDescription = (TextView) itemView.findViewById(R.id.img_description);
            imageItem = (CardImageView) itemView.findViewById(R.id.img_grid_item);
            progressGif = (GifImageView) itemView.findViewById(R.id.progressGif);
            imageIteamNetwork = (NetworkGifImageView) itemView.findViewById(R.id.img_grid_item_network);
        }
    }

    public void resetImages(ArrayList<epicara.younility.model.Image> imageModellist)
    {
        mImageModelList.clear();
        mImageModelList.addAll(imageModellist);
    }

    public void addMoreImages(ArrayList<epicara.younility.model.Image> imageModellist)
    {
        mImageModelList.addAll(imageModellist);
        notifyDataSetChanged();
    }

    public ArrayList<epicara.younility.model.Image> getImageModelList()
    {
        return mImageModelList;
    }

    // Delegated Methods

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.imgur_grid_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        epicara.younility.model.Image model = mImageModelList.get(position);

        // Set item views based on the data model

        TextView textView = holder.imageDescription;
        textView.setText(model.getTitle());

        // Progress gif

        GifImageView progressGif = holder.progressGif;
        progressGif.setVisibility(View.GONE);

        // Set image view

        CardImageView imageView = holder.imageItem;
        NetworkGifImageView imageViewNetwork = holder.imageIteamNetwork;

        imageView.setVisibility(View.GONE);
        imageViewNetwork.setVisibility(View.GONE);

        // Get image url

        String imageUrl = model.getLink();



        // Load image from Universal Image Loader

        imageView.setImageDrawable(null);

        if(model.isGif())
        {
            imageViewNetwork.setVisibility(View.VISIBLE);
            imageViewNetwork.setImageUrl(imageUrl, mImageLoader);
        }
        else {

            imageView.setVisibility(View.VISIBLE);
            ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
            imageLoader.displayImage(imageUrl, imageView);
            imageView.setAspectRatio(1.0f);
        }


//        progressGif.setVisibility(View.VISIBLE);
//
//        Glide.with(holder.imageItem.getContext())
//                .load("http://i.giphy.com/WLIerIoiUJL1K.gif")
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        progressGif.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
//                .into(imageView);
//
//        imageView.setAspectRatio(1.0f);

//            Glide.with(holder.imageItem.getContext())
//                    .load(imageUrl)
//                    .placeholder(R.drawable.ic_loader)
//                    .into(imageView);

//        Glide.with(holder.imageItem.getContext())
//                .load(imageUrl)
//                .into(imageView);

//        if(model.isGif())
//        {
//            Glide.with(holder.imageItem.getContext())
//                    .load(imageUrl)
//                    .asGif()
//                    .placeholder(R.drawable.ic_loader)
//                    .into(imageView);
//
//        }else
//        {
//            Glide.with(holder.imageItem.getContext())
//                    .load(imageUrl)
//                    .into(imageView);
//        }

        imageView.setAspectRatio(1.0f);


//        In case someone wants to use glide or Picasso

//        Glide.with(holder.imageItem.getContext())
//                .load(imageUrl)
//                .placeholder(circularLoader)
//                .crossFade()
//                .signature(new StringSignature(imageUrl))
//                .into(imageView);
//
//        Picasso.with(holder.imageItem.getContext())
//                .load(imageUrl)
//                .placeholder(circularLoader)
//                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if(mImageModelList == null)
        {
            return  0;
        }
        else
        {
            return mImageModelList.size();
        }
    }

}