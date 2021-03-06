package com.future.bestmovies.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavouritesContract {
    private FavouritesContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.future.bestmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.future.bestmovies/movie/ is a valid path for
     * looking at movie data. content://com.future.bestmovies/ratings/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "ratings".
     */
    public static final String PATH_MOVIES = "movies";

    // Inner class that defines constant values for the movie details database table.
    // Each entry in the table represents a single movie.
    public static abstract class MovieDetailsEntry implements BaseColumns {
        // The content URI to access the movie data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
        // The MIME type of the {@link #CONTENT_URI} for a list of movies.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        // The MIME type of the {@link #CONTENT_URI} for a single movie.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public final static String TABLE_NAME =                 "movie_details";
        public final static String _ID = BaseColumns._ID;                           // Type: INTEGER (Unique ID)
        public final static String COLUMN_MOVIE_ID =            "movie_id";         // Type: INTEGER
        public final static String COLUMN_TITLE =               "title";            // Type: TEXT
        public final static String COLUMN_POSTER_PATH =         "poster_path";      // Type: TEXT
        public final static String COLUMN_BACKDROP_PATH =       "backdrop_path";    // Type: TEXT
        public final static String COLUMN_PLOT =                "overview";         // Type: TEXT
        public final static String COLUMN_RATINGS =             "ratings";          // Type: DOUBLE
        public final static String COLUMN_RELEASE_DATE =        "release_date";     // Type: TEXT
        public final static String COLUMN_GENRES =              "genres";           // Type: TEXT
        public final static String COLUMN_LANGUAGE =            "language";         // Type: TEXT
        public final static String COLUMN_RUNTIME =             "runtime";          // Type: INTEGER
    }

    // Inner class that defines constant values for the movie cast database table.
    // Each entry in the table represents a cast member.
    public static final String PATH_CAST = "cast";

    public static abstract class CastEntry implements BaseColumns {
        // The content URI to access the section data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CAST);
        // The MIME type of the {@link #CONTENT_URI} for a cast list.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;
        // The MIME type of the {@link #CONTENT_URI} for a single cast member.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;

        public final static String TABLE_NAME =                 "movie_cast";
        public final static String _ID = BaseColumns._ID;                           // Type: INTEGER (Unique ID)
        public final static String COLUMN_MOVIE_ID =            "movie_id";         // Type: INTEGER
        public final static String COLUMN_ACTOR_NAME =          "actor_name";       // Type: TEXT
        public final static String COLUMN_CHARACTER_NAME =      "character_name";   // Type: TEXT
        public final static String COLUMN_ACTOR_ID =            "actor_id";         // Type: INTEGER
        public final static String COLUMN_IMAGE_PROFILE_PATH =  "profile_path";     // Type: TEXT
    }

    // Inner class that defines constant values for the movie reviews database table.
    // Each entry in the table represents a single review.
    public static final String PATH_REVIEWS = "reviews";

    public static abstract class ReviewsEntry implements BaseColumns {
        // The content URI to access the section data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REVIEWS);
        // The MIME type of the {@link #CONTENT_URI} for a list of reviews.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        // The MIME type of the {@link #CONTENT_URI} for a single review.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public final static String TABLE_NAME =                 "movie_reviews";
        public final static String _ID = BaseColumns._ID;                           // Type: INTEGER (Unique ID)
        public final static String COLUMN_MOVIE_ID =            "movie_id";         // Type: INTEGER
        public final static String COLUMN_AUTHOR =              "author";           // Type: TEXT
        public final static String COLUMN_CONTENT =             "content";          // Type: TEXT
    }

    // Inner class that defines constant values for the movie video database table.
    // Each entry in the table represents a single video.
    public static final String PATH_VIDEOS = "videos";

    public static abstract class VideosEntry implements BaseColumns {
        // The content URI to access the section data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VIDEOS);
        // The MIME type of the {@link #CONTENT_URI} for a list of videos.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;
        // The MIME type of the {@link #CONTENT_URI} for a single video.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;

        public final static String TABLE_NAME =                 "movie_videos";
        public final static String _ID = BaseColumns._ID;                           // Type: INTEGER (Unique ID)
        public final static String COLUMN_MOVIE_ID =            "movie_id";         // Type: INTEGER
        public final static String COLUMN_VIDEO_KEY =           "video_key";        // Type: TEXT
        public final static String COLUMN_VIDEO_NAME =          "video_name";       // Type: TEXT
        public final static String COLUMN_VIDEO_TYPE =          "video_type";       // Type: TEXT
    }

    // Inner class that defines constant values for the actors database table.
    // Each entry in the table represents a single actor.
    public static final String PATH_ACTORS = "actors";

    public static abstract class ActorsEntry implements BaseColumns {
        // The content URI to access the section data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACTORS);
        // The MIME type of the {@link #CONTENT_URI} for a list of actors.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTORS;
        // The MIME type of the {@link #CONTENT_URI} for a single actor.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTORS;

        public final static String TABLE_NAME =                 "actor_details";
        public final static String _ID = BaseColumns._ID;                           // Type: INTEGER (Unique ID)
        public final static String COLUMN_ACTOR_ID =            "actor_id";         // Type: INTEGER
        public final static String COLUMN_BIRTHDAY =            "birthday";         // Type: TEXT
        public final static String COLUMN_DEATH_DAY =           "deathday";         // Type: TEXT
        public final static String COLUMN_GENDER =              "gender";           // Type: INTEGER
        public final static String COLUMN_NAME =                "name";             // Type: TEXT
        public final static String COLUMN_BIOGRAPHY =           "biography";        // Type: TEXT
        public final static String COLUMN_PLACE_OF_BIRTH =      "place_of_birth";   // Type: TEXT
        public final static String COLUMN_PROFILE_PATH =        "profile_path";     // Type: TEXT
    }

    // Inner class that defines constant values for the actors credits database table.
    // Each entry in the table represents a single credit.
    public static final String PATH_CREDITS = "credits";

    public static abstract class CreditsEntry implements BaseColumns {
        // The content URI to access the section data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CREDITS);
        // The MIME type of the {@link #CONTENT_URI} for a list of credits.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDITS;
        // The MIME type of the {@link #CONTENT_URI} for a single credit.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDITS;

        public final static String TABLE_NAME =                 "movie_credits";
        public final static String _ID = BaseColumns._ID;                           // Type: INTEGER (Unique ID)
        public final static String COLUMN_ACTOR_ID =            "actor_id";         // Type: INTEGER
        public final static String COLUMN_CHARACTER =           "character";        // Type: TEXT
        public final static String COLUMN_MOVIE_ID =            "movie_id";         // Type: INTEGER
        public final static String COLUMN_POSTER_PATH =         "poster_path";      // Type: TEXT
        public final static String COLUMN_RELEASE_DATE =        "release_date";     // Type: TEXT
        public final static String COLUMN_TITLE =               "title";            // Type: TEXT
    }

    // Build a specific Uri for accessing table contents, using the content uri for
    // the selected table and a movieId.
    public static Uri buildUriWithId(Uri contentUri, int movieId) {
        return contentUri.buildUpon()
                .appendPath(Integer.toString(movieId))
                .build();
    }
}
