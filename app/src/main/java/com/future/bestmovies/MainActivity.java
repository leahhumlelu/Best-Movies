package com.future.bestmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.future.bestmovies.movie.FavouriteActorsAdapter;
import com.future.bestmovies.movie.CategoryAdapter;
import com.future.bestmovies.movie.FavouriteMoviesAdapter;
import com.future.bestmovies.movie.Movie;
import com.future.bestmovies.movie.CategoryLoader;
import com.future.bestmovies.movie.MoviePreferences;
import com.future.bestmovies.utils.ImageUtils;
import com.future.bestmovies.utils.NetworkUtils;
import com.future.bestmovies.utils.ScreenUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.future.bestmovies.data.FavouritesContract.*;


public class MainActivity extends AppCompatActivity implements
        CategoryAdapter.GridItemClickListener,
        FavouriteMoviesAdapter.GridItemClickListener,
        FavouriteActorsAdapter.GridItemClickListener,
        LoaderManager.LoaderCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TITLE_KEY = "title";
    private static final String POSITION_KEY = "current_position";

    private Bundle mBundleState;
    private Parcelable state;

    // Movie categories
    private static final String CATEGORY_POPULAR = "popular";
    private static final String CATEGORY_TOP_RATED = "top_rated";
    private static final String CATEGORY_UPCOMING = "upcoming";
    private static final String CATEGORY_NOW_PLAYING = "now_playing";
    private static final String CATEGORY_FAVOURITE_ACTORS = "favourite_actors";
    private static final String CATEGORY_FAVOURITE_MOVIES = "favourite_movies";

    // Loaders
    private static final int CATEGORY_LOADER_ID = 24;
    private static final int FAVOURITE_ACTORS_LOADER_ID = 136;
    private static final int FAVOURITE_MOVIES_LOADER_ID = 805;
    private static final String CURRENT_LOADED_ID = "loader_id";
    private int mCurrentLoaderId;

    // Actors cursor projection
    private static final String[] FAVOURITE_ACTORS_PROJECTION = {
            ActorsEntry.COLUMN_ACTOR_ID,
            ActorsEntry.COLUMN_NAME,
            ActorsEntry.COLUMN_PROFILE_PATH
    };

    // Movies cursor projection
    private static final String[] FAVOURITE_MOVIES_PROJECTION = {
            MovieDetailsEntry.COLUMN_MOVIE_ID,
            MovieDetailsEntry.COLUMN_POSTER_PATH
    };

    @BindView(R.id.movies_rv)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.messages_tv)
    TextView mMessagesTextView;
    @BindView(R.id.no_connection_cloud_iv)
    ImageView mNoConnectionImageView;
    @BindView(R.id.loading_pb)
    ProgressBar mLoading;
    private CategoryAdapter mCategoryAdapter;
    private FavouriteMoviesAdapter mMoviesAdapter;
    private FavouriteActorsAdapter mActorsAdapter;
    private GridLayoutManager mGridLayoutManager;
    private int mPosition = RecyclerView.NO_POSITION;

    // Infinite scrolling variables
    private boolean isScrolling = false;
    private int visibleItems;
    private int totalItems;
    private int scrolledUpItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLoading.setVisibility(View.VISIBLE);
        // The layout manager for our RecyclerView will be a GridLayout, so we can display our movies
        // on columns. The number of columns is dictated by the orientation and size of the device
        mGridLayoutManager = new GridLayoutManager(
                this,
                ScreenUtils.getNumberOfColumns(this, 200, 2));
        mMoviesRecyclerView.setLayoutManager(mGridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mCategoryAdapter = new CategoryAdapter(this, this);
        mMoviesAdapter = new FavouriteMoviesAdapter(this, this);
        mActorsAdapter = new FavouriteActorsAdapter(this, this);
        //mMoviesRecyclerView.setAdapter(mCategoryAdapter);

        // Check if preference "image_width" was create before, if not, proceed.
        if (!MoviePreferences.isImageWidthAvailable(this)) {
            // Create an image width preference for our RecyclerView
            // This preference is very useful to our RecyclerView, so we can load all the images
            // into it heaving the same width. The image width will be perfect(or almost perfect)
            // for the device we are using. We measure once and use it as many times we want.
            MoviePreferences.setImageWidthForRecyclerView(
                    this,
                    ImageUtils.getImageWidth(this, ImageUtils.POSTER));
        }

        // Every time we create this activity we set the page number of our results to be 0
        MoviePreferences.setLastPageNumber(this, 1);

        if (savedInstanceState == null) {
            // Set current loader id and title, depending on preferred "query type" (aka: "movie category" or "sort order")
            switch (MoviePreferences.getPreferredQueryType(this)) {
                case CATEGORY_POPULAR:
                    mCurrentLoaderId = CATEGORY_LOADER_ID;
                    setTitle(R.string.menu_popular);
                    mCategoryAdapter = new CategoryAdapter(this, this);
                    mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                    break;
                case CATEGORY_TOP_RATED:
                    mCurrentLoaderId = CATEGORY_LOADER_ID;
                    setTitle(R.string.menu_top_rated);
                    mCategoryAdapter = new CategoryAdapter(this, this);
                    mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                    break;
                case CATEGORY_UPCOMING:
                    mCurrentLoaderId = CATEGORY_LOADER_ID;
                    setTitle(R.string.menu_upcoming);
                    mCategoryAdapter = new CategoryAdapter(this, this);
                    mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                    break;
                case CATEGORY_NOW_PLAYING:
                    mCurrentLoaderId = CATEGORY_LOADER_ID;
                    setTitle(R.string.menu_now_playing);
                    mCategoryAdapter = new CategoryAdapter(this, this);
                    mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                    break;
                case CATEGORY_FAVOURITE_ACTORS:
                    mCurrentLoaderId = FAVOURITE_ACTORS_LOADER_ID;
                    setTitle(R.string.menu_favourite_actors);
                    mActorsAdapter = new FavouriteActorsAdapter(this, this);
                    mMoviesRecyclerView.setAdapter(mActorsAdapter);
                    break;
                case CATEGORY_FAVOURITE_MOVIES:
                    mCurrentLoaderId = FAVOURITE_MOVIES_LOADER_ID;
                    setTitle(R.string.menu_favourite_movies);
                    mMoviesAdapter = new FavouriteMoviesAdapter(this, this);
                    mMoviesRecyclerView.setAdapter(mMoviesAdapter);
                    break;
            }

            // Fetch movie data, using the selected loader id
            fetchMovies(this);
        }

        // To create an infinite scrolling effect, we add an OnScrollListener to our RecyclerView
        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // This scroll listener will be used only if the selected category is not favourite actors or movies
                if (!TextUtils.equals(MoviePreferences.getPreferredQueryType(getApplicationContext()), CATEGORY_FAVOURITE_ACTORS) &&
                        !TextUtils.equals(MoviePreferences.getPreferredQueryType(getApplicationContext()), CATEGORY_FAVOURITE_MOVIES)) {
                    // To be able to load data in advance, before the user gets to the bottom of our
                    // present results, we have to know how many items are visible on the screen, how
                    // many items are in total and how many items are already scrolled out of the screen
                    visibleItems = mGridLayoutManager.getChildCount();
                    totalItems = mGridLayoutManager.getItemCount();
                    scrolledUpItems = mGridLayoutManager.findFirstVisibleItemPosition();

                    // We set a threshold, to help us know that the user is about to get to the end of
                    // the list.
                    int threshold = 5;

                    // If the user is still scrolling and the the Threshold is bigger or equal with the
                    // totalItems - visibleItems - scrolledUpItems, we know we have to load new Movies
                    if (isScrolling && (threshold >= totalItems - visibleItems - scrolledUpItems)) {
                        isScrolling = false;
                        Log.v(TAG, "Load new movies!");
                        loadNewMovies();
                    }
                }
            }
        });
    }

    // TODO 1: New settings for deleting all favourite actors and all movies
    // TODO 2: Search should filter TvShows or App should include TvShows

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_LOADED_ID, mCurrentLoaderId);
        outState.putString(TITLE_KEY, getTitle().toString());

        outState.putParcelable(POSITION_KEY, mGridLayoutManager.onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_LOADED_ID)) {
                mCurrentLoaderId = savedInstanceState.getInt(CURRENT_LOADED_ID);
                switch (mCurrentLoaderId) {
                    case CATEGORY_LOADER_ID:
                        //mCategoryAdapter = new CategoryAdapter(this, this);
                        mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                        break;
                    case FAVOURITE_ACTORS_LOADER_ID:
                        //mActorsAdapter = new FavouriteActorsAdapter(this, this);
                        mMoviesRecyclerView.setAdapter(mActorsAdapter);
                        break;
                    case FAVOURITE_MOVIES_LOADER_ID:
                        //mMoviesAdapter = new FavouriteMoviesAdapter(this, this);
                        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
                        break;
                }
                fetchMovies(this);
            }

            if (savedInstanceState.containsKey(TITLE_KEY))
                setTitle(savedInstanceState.getString(TITLE_KEY));

            if (savedInstanceState.containsKey(POSITION_KEY)) {
                mGridLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(POSITION_KEY));
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBundleState = new Bundle();

        mBundleState.putParcelable(POSITION_KEY, mGridLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBundleState != null) {
            if (mBundleState.containsKey(POSITION_KEY))
                mGridLayoutManager.onRestoreInstanceState(mBundleState.getParcelable(POSITION_KEY));
        }
    }

    private void loadNewMovies() {
        mLoading.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Before we fetch data, we need the last page number that was loaded in our RecyclerView,
                // increment it by 1 and save it in a preference for next data fetching
                int nextPage = MoviePreferences.getLastPageNumber(getApplicationContext()) + 1;
                MoviePreferences.setLastPageNumber(getApplicationContext(), nextPage);

                fetchMovies(getApplicationContext());
                mLoading.setVisibility(View.GONE);
            }
        }, 1000);
    }

    // Fetch data or show connection error
    private void fetchMovies(Context context) {
        // If there is a network connection, fetch data
        if (NetworkUtils.isConnected(context) && mCurrentLoaderId != FAVOURITE_ACTORS_LOADER_ID && mCurrentLoaderId != FAVOURITE_MOVIES_LOADER_ID) {
            showMovies();

            //Init or restart loader
            getSupportLoaderManager().restartLoader(mCurrentLoaderId, null, this);
        }
        // If selected loader id is FAVOURITES_LOADER_ID, we don't need internet connection, so we
        // showMovies and start favourites loader
        else if (mCurrentLoaderId == FAVOURITE_ACTORS_LOADER_ID || mCurrentLoaderId == FAVOURITE_MOVIES_LOADER_ID) {
            showMovies();

            //Init or restart loader
            getSupportLoaderManager().restartLoader(mCurrentLoaderId, null, this);
        }
        // If no connection and the loader id is not FAVOURITES_LOADER_ID
        else {
            // Hide loading indicator, hide data and display connection error message
            showError();

            // Update message TextView with no connection error message
            mMessagesTextView.setText(R.string.no_internet);

            // Every time we have a connection error, we set the page number of our results to be 0
            MoviePreferences.setLastPageNumber(this, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putExtra(SearchActivity.SEARCH_QUERY_KEY, query);
                startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem selectedItem;
        switch (MoviePreferences.getPreferredQueryType(this)) {
            case CATEGORY_POPULAR:
                selectedItem = menu.findItem(R.id.action_popular);
                selectedItem.setIcon(R.drawable.ic_check);
                break;
            case CATEGORY_TOP_RATED:
                selectedItem = menu.findItem(R.id.action_top_rated);
                selectedItem.setIcon(R.drawable.ic_check);
                break;
            case CATEGORY_UPCOMING:
                selectedItem = menu.findItem(R.id.action_upcoming);
                selectedItem.setIcon(R.drawable.ic_check);
                break;
            case CATEGORY_NOW_PLAYING:
                selectedItem = menu.findItem(R.id.action_now_playing);
                selectedItem.setIcon(R.drawable.ic_check);
                break;
            case CATEGORY_FAVOURITE_ACTORS:
                selectedItem = menu.findItem(R.id.action_favourite_actors);
                selectedItem.setIcon(R.drawable.ic_check);
                break;
            case CATEGORY_FAVOURITE_MOVIES:
                selectedItem = menu.findItem(R.id.action_favourite_movies);
                selectedItem.setIcon(R.drawable.ic_check);
                break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                MoviePreferences.setPreferredQueryType(this, getString(R.string.category_popular));
                MoviePreferences.setLastPageNumber(this, 1);
                getSupportLoaderManager().destroyLoader(FAVOURITE_ACTORS_LOADER_ID);
                getSupportLoaderManager().destroyLoader(FAVOURITE_MOVIES_LOADER_ID);
                mCurrentLoaderId = CATEGORY_LOADER_ID;
                mCategoryAdapter = new CategoryAdapter(this, this);
                mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                fetchMovies(getApplicationContext());
                setTitle(item.getTitle());
                invalidateOptionsMenu();
                break;

            case R.id.action_top_rated:
                MoviePreferences.setPreferredQueryType(this, getString(R.string.category_top_rated));
                MoviePreferences.setLastPageNumber(this, 1);
                getSupportLoaderManager().destroyLoader(FAVOURITE_ACTORS_LOADER_ID);
                getSupportLoaderManager().destroyLoader(FAVOURITE_MOVIES_LOADER_ID);
                mCurrentLoaderId = CATEGORY_LOADER_ID;
                mCategoryAdapter = new CategoryAdapter(this, this);
                mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                fetchMovies(getApplicationContext());
                setTitle(item.getTitle());
                invalidateOptionsMenu();
                break;

            case R.id.action_upcoming:
                MoviePreferences.setPreferredQueryType(this, getString(R.string.category_upcoming));
                MoviePreferences.setLastPageNumber(this, 1);
                getSupportLoaderManager().destroyLoader(FAVOURITE_ACTORS_LOADER_ID);
                getSupportLoaderManager().destroyLoader(FAVOURITE_MOVIES_LOADER_ID);
                mCurrentLoaderId = CATEGORY_LOADER_ID;
                mCategoryAdapter = new CategoryAdapter(this, this);
                mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                fetchMovies(getApplicationContext());
                setTitle(item.getTitle());
                invalidateOptionsMenu();
                break;

            case R.id.action_now_playing:
                MoviePreferences.setPreferredQueryType(this, getString(R.string.category_now_playing));
                MoviePreferences.setLastPageNumber(this, 1);
                getSupportLoaderManager().destroyLoader(FAVOURITE_ACTORS_LOADER_ID);
                getSupportLoaderManager().destroyLoader(FAVOURITE_MOVIES_LOADER_ID);
                mCurrentLoaderId = CATEGORY_LOADER_ID;
                mCategoryAdapter = new CategoryAdapter(this, this);
                mMoviesRecyclerView.setAdapter(mCategoryAdapter);
                fetchMovies(getApplicationContext());
                setTitle(item.getTitle());
                invalidateOptionsMenu();
                break;

            case R.id.action_favourite_actors:
                MoviePreferences.setPreferredQueryType(this, getString(R.string.category_favourite_actors));
                getSupportLoaderManager().destroyLoader(CATEGORY_LOADER_ID);
                getSupportLoaderManager().destroyLoader(FAVOURITE_MOVIES_LOADER_ID);
                mCurrentLoaderId = FAVOURITE_ACTORS_LOADER_ID;
                fetchMovies(getApplicationContext());
                setTitle(item.getTitle());
                invalidateOptionsMenu();
                break;

            case R.id.action_favourite_movies:
                MoviePreferences.setPreferredQueryType(this, getString(R.string.category_favourite_movies));
                getSupportLoaderManager().destroyLoader(CATEGORY_LOADER_ID);
                getSupportLoaderManager().destroyLoader(FAVOURITE_ACTORS_LOADER_ID);
                mCurrentLoaderId = FAVOURITE_MOVIES_LOADER_ID;
                fetchMovies(getApplicationContext());
                setTitle(item.getTitle());
                invalidateOptionsMenu();
                break;

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Every time we click a movie poster, we create an intent and pass a Movie object along with it,
    // so we can display all the information that we received about it, without heaving to fetch
    // more data from movie API server
    @Override
    public void onGridItemClick(Movie movieClicked) {
        Intent movieDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        movieDetailsIntent.putExtra(DetailsActivity.MOVIE_ID_KEY, movieClicked.getMovieId());
        movieDetailsIntent.putExtra(DetailsActivity.MOVIE_TITLE_KEY, movieClicked.getTitle());
        startActivity(movieDetailsIntent);
    }

    @Override
    public void onMovieItemClick(int movieId) {
        Intent movieDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        movieDetailsIntent.putExtra(DetailsActivity.MOVIE_ID_KEY, movieId);
        startActivity(movieDetailsIntent);
    }

    @Override
    public void onActorItemClick(int actorId) {
        Intent actorIntent = new Intent(MainActivity.this, ProfileActivity.class);
        actorIntent.putExtra(DetailsActivity.ACTOR_ID_KEY, actorId);
        startActivity(actorIntent);
    }

    // Show movie data and hide no connection icon and message
    private void showMovies() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mNoConnectionImageView.setVisibility(View.INVISIBLE);
        mNoConnectionImageView.setImageResource(R.drawable.ic_cloud_off);
        mMessagesTextView.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    // Hide the movie data and loading indicator and show error message
    private void showError() {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mNoConnectionImageView.setVisibility(View.VISIBLE);
        mNoConnectionImageView.setImageResource(R.drawable.ic_cloud_off);
        mMessagesTextView.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case CATEGORY_LOADER_ID:
                // If the loaded id matches movies loader, return a new movie category loader
                return new CategoryLoader(getApplicationContext());

            case FAVOURITE_ACTORS_LOADER_ID:
                // If the loader id matches favourite actors loader, return a cursor loader
                return new CursorLoader(
                        getApplicationContext(),
                        ActorsEntry.CONTENT_URI,
                        FAVOURITE_ACTORS_PROJECTION,
                        null,
                        null,
                        null);

            case FAVOURITE_MOVIES_LOADER_ID:
                // If the loader id matches favourites loader, return a cursor loader
                return new CursorLoader(
                        getApplicationContext(),
                        MovieDetailsEntry.CONTENT_URI,
                        FAVOURITE_MOVIES_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        if (data != null) {
            mLoading.setVisibility(View.GONE);
        }

        switch (loader.getId()) {
            case CATEGORY_LOADER_ID:
                mNoConnectionImageView.setVisibility(View.INVISIBLE);
                mMessagesTextView.setVisibility(View.INVISIBLE);

                // Every time we get new results we have 2 possibilities
                int currentPage = MoviePreferences.getLastPageNumber(getApplicationContext());
                // If currentPage is "1", we know that the user has changed the movie category or uses the
                // app for the first time. In this situation we swap the Movie array with the new data
                if (currentPage == 1) {
                    mCategoryAdapter.swapMovies((ArrayList<Movie>) data);
                } else {
                    // Otherwise, we add the new data to the old data, creating an infinite scrolling effect
                    if (currentPage > 1)
                        mCategoryAdapter.addMovies((ArrayList<Movie>) data);
                }

                // If the RecyclerView has no position, we assume the first position in the list
                // and set the RecyclerView at the beginning of results
                if (mPosition == RecyclerView.NO_POSITION) {
                    mPosition = 0;
                    mMoviesRecyclerView.smoothScrollToPosition(mPosition);
                }

                break;

            case FAVOURITE_ACTORS_LOADER_ID:
                mActorsAdapter = new FavouriteActorsAdapter(this, this);
                mMoviesRecyclerView.setAdapter(mActorsAdapter);

                mActorsAdapter.swapActors((Cursor) data);

                if (data != null && ((Cursor) data).getCount() == 0) {
                    mNoConnectionImageView.setVisibility(View.VISIBLE);
                    mNoConnectionImageView.setImageResource(R.drawable.ic_star);
                    mNoConnectionImageView.setContentDescription(getString(R.string.no_favourites_icon));
                    mMessagesTextView.setVisibility(View.VISIBLE);
                    mMessagesTextView.setText(R.string.no_favourite_actors);
                } else {
                    mNoConnectionImageView.setVisibility(View.INVISIBLE);
                    mMessagesTextView.setVisibility(View.INVISIBLE);

                    // If the RecyclerView has no position, we assume the first position in the list
                    if (mPosition == RecyclerView.NO_POSITION) {
                        mPosition = 0;
                        // Scroll the RecyclerView to mPosition
                        mMoviesRecyclerView.smoothScrollToPosition(mPosition);
                    }
                }

                break;

            case FAVOURITE_MOVIES_LOADER_ID:
                mMoviesAdapter = new FavouriteMoviesAdapter(this, this);
                mMoviesRecyclerView.setAdapter(mMoviesAdapter);

                mMoviesAdapter.swapMovies((Cursor) data);

                if (data != null && ((Cursor) data).getCount() == 0) {
                    mNoConnectionImageView.setVisibility(View.VISIBLE);
                    mNoConnectionImageView.setImageResource(R.drawable.ic_favorite);
                    mNoConnectionImageView.setContentDescription(getString(R.string.no_favourites_icon));
                    mMessagesTextView.setVisibility(View.VISIBLE);
                    mMessagesTextView.setText(R.string.no_favourite_movies);
                } else {
                    mNoConnectionImageView.setVisibility(View.INVISIBLE);
                    mMessagesTextView.setVisibility(View.INVISIBLE);

                    // If the RecyclerView has no position, we assume the first position in the list
                    if (mPosition == RecyclerView.NO_POSITION) {
                        mPosition = 0;
                        // Scroll the RecyclerView to mPosition
                        mMoviesRecyclerView.smoothScrollToPosition(mPosition);
                    }
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
//        switch (loader.getId()) {
//            case FAVOURITE_ACTORS_LOADER_ID:
//                mActorsAdapter.swapActors(null);
//                break;
//            case FAVOURITE_MOVIES_LOADER_ID:
//                mMoviesAdapter.swapCursor(null);
//                break;
//            default:
//                break;
//        }
    }
}