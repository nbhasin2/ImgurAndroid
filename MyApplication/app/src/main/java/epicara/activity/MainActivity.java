package epicara.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

import epicara.Global.Constants;
import epicara.Network.Api;
import epicara.Network.Client;
import epicara.UI.ItemClickSupport;
import epicara.adapter.ImageAdapter;
import epicara.younility.model.Image;
import epicara.younility.model.ImageModel;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Header;

// Helpful link -

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Api imgurApi;
    private Call<ImageModel> getImages;
    private ImageAdapter mImageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private RecyclerView gridView;
    private int initialPage = 1;
    private int firstVisibleItemsGrid[] = new int[2];
    private OnResultsScrollListener onResultsScrollListener;
    private String currentType = Constants.MEME;
    private Boolean resetData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPurpuleMix)));
        gridView = (RecyclerView) findViewById(R.id.imgur_recyclerview);

        setupView();
    }

    private void setupView()
    {
        // Universal Image Loader Configration

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());


        setSupportActionBar(toolbar);

        // Setup Api
        imgurApi = Client.getClient();

        // Setup reload FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reloading Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                initialPage = 1;
//                currentType = Constants.MEME;

                loadImages(currentType, true);

            }
        });

        // Drawer setup

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Load Images
        loadImages(currentType, false);

        // Grid View Layout
        mGridLayoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        mGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        gridView.setLayoutManager(mGridLayoutManager);

        // Grid View Scroll Listener (pagination)
        onResultsScrollListener = new OnResultsScrollListener(mGridLayoutManager);
        gridView.addOnScrollListener(onResultsScrollListener);

        // Grid View on item click listener
        itemClickSupport(gridView);
    }

    private void itemClickSupport(RecyclerView recyclerView)
    {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent intent = new Intent(getBaseContext(), PhotoViewerActivity.class);
                        Image image = ((ImageAdapter) recyclerView.getAdapter()).getImageModelList().get(position);
                        intent.putExtra(Constants.IMAGE_LINK, image.getLink());
                        intent.putExtra(Constants.IMAGE_IS_GIF, image.isGif());
                        startActivity(intent);
                    }
                }
        );
    }

    private void loadImages(String type, final Boolean forceReset) {

        if(!type.equals(currentType))
        {
            initialPage = 1;
            resetData = true;
        }

        if(type.equals(Constants.FUNNY))
        {
            currentType = Constants.FUNNY;
            getImages = imgurApi.getFunny(initialPage);
        }
        else if(type.equals(Constants.MEME))
        {
            currentType = Constants.MEME;
            getImages = imgurApi.getMeme(initialPage);

        }

//        Log.d("MainActivity", "Page Num = " + initialPage);

        getImages.enqueue(new Callback<ImageModel>() {

            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
//                Log.d("MainActivity", "Response Code = " + response.code());
//                Log.d("MainActivity", "Response Body = " + response.body().getImages());
                if((resetData == true || forceReset == true) && response.isSuccessful() && initialPage < 2 && mImageAdapter != null)
                {
                    gridView.removeOnScrollListener(onResultsScrollListener);
                    onResultsScrollListener = new OnResultsScrollListener(mGridLayoutManager);
                    gridView.addOnScrollListener(onResultsScrollListener);

                    ((ImageAdapter) gridView.getAdapter()).resetImages((ArrayList<Image>) response.body().getImages());
                    gridView.getAdapter().notifyDataSetChanged();

                }else if (response.isSuccessful() && initialPage < 2) {
                    mImageAdapter = new ImageAdapter((ArrayList<Image>) response.body().getImages(),getApplicationContext());
                    gridView.setAdapter(mImageAdapter);
                    gridView.getAdapter().notifyDataSetChanged();

                }
                else if(response.isSuccessful() && initialPage >= 2)
                {
                    mImageAdapter.addMoreImages((ArrayList<Image>) response.body().getImages());
                }

                initialPage++;
                resetData = false;
                Headers headerList = response.headers();

//                Log.d("MainActivity", "Header Code = " + headerList.toString());

            }

            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {

                Log.d("MainActivity", t.getMessage());
            }

        });
    }

    //http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview

    private class OnResultsScrollListener extends RecyclerView.OnScrollListener {

        private int previousTotal = 0; // The total number of items in the dataset after the last load
        private boolean loading = true; // True if we are still waiting for the last set of data to load.
        private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
        int visibleItemCount, totalItemCount;

        private StaggeredGridLayoutManager mLayoutManager;

        public OnResultsScrollListener(StaggeredGridLayoutManager mLayoutManager) {
            this.mLayoutManager = mLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if(mImageAdapter.getItemCount() > 0 && mLayoutManager != null)
            {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();


                firstVisibleItemsGrid = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItemsGrid);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - firstVisibleItemsGrid[0]) <= (firstVisibleItemsGrid[0] + visibleThreshold)) {
                    loadMoreImages();
                    loading = true;
                }
            }

        }
        public void reset(int previousTotal, boolean loading) {
            this.previousTotal = previousTotal;
            this.loading = loading;
        }
        private boolean isEnd(){
            return !gridView.canScrollVertically(1) && firstVisibleItemsGrid[0] != 0;
        }
        private void loadMoreImages(){
            loadImages(currentType, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResultsScrollListener.reset(0,true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meme) {

            loadImages(Constants.MEME, false);

        } else if (id == R.id.nav_funny) {

            loadImages(Constants.FUNNY, false);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
