package com.future.bestmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.future.bestmovies.credits.Credits;
import com.future.bestmovies.credits.CreditsAdapter;

import static com.future.bestmovies.data.FavouritesContract.*;

public class FavouritesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favourites.db";

    // Movie Details Table
    private static final String SQL_CREATE_MOVIE_DETAILS_TABLE =
            "CREATE TABLE " + MovieDetailsEntry.TABLE_NAME  + " ("                                      +
                    MovieDetailsEntry._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                    MovieDetailsEntry.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, "                     +
                    MovieDetailsEntry.COLUMN_BACKDROP_PATH  + " TEXT, "                                 +
                    MovieDetailsEntry.COLUMN_GENRES         + " TEXT, "                                 +
                    MovieDetailsEntry.COLUMN_LANGUAGE       + " TEXT, "                                 +
                    MovieDetailsEntry.COLUMN_PLOT           + " TEXT, "                                 +
                    MovieDetailsEntry.COLUMN_POSTER_PATH    + " TEXT, "                                 +
                    MovieDetailsEntry.COLUMN_RATINGS        + " REAL, "                                 +
                    MovieDetailsEntry.COLUMN_RELEASE_DATE   + " TEXT, "                                 +
                    MovieDetailsEntry.COLUMN_RUNTIME        + " INTEGER, "                              +
                    MovieDetailsEntry.COLUMN_TITLE          + " TEXT NOT NULL, "                        +
                    /*
                    * To ensure this table can only contain one movie entry per with a movie_id,
                    * we declare the movie_id column to be unique. We also specify "ON CONFLICT REPLACE".
                    * This tells SQLite that if we have a movie entry for a certain id and we attempt
                    * to insert another movie entry with that id, we replace the old movie entry.
                    */
                    " UNIQUE (" + MovieDetailsEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

    private static final String SQL_DELETE_MOVIE_DETAILS_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieDetailsEntry.TABLE_NAME;

    // Cast Table
    private static final String SQL_CREATE_CAST_TABLE =
            "CREATE TABLE " + CastEntry.TABLE_NAME          + " ("                                      +
                    CastEntry._ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                    CastEntry.COLUMN_MOVIE_ID               + " INTEGER NOT NULL, "                     +
                    CastEntry.COLUMN_ACTOR_NAME             + " TEXT, "                                 +
                    CastEntry.COLUMN_CHARACTER_NAME         + " TEXT, "                                 +
                    CastEntry.COLUMN_IMAGE_PROFILE_PATH     + " TEXT, "                                 +
                    CastEntry.COLUMN_ACTOR_ID               + " INTEGER);";

    private static final String SQL_DELETE_CAST_ENTRIES =
            "DROP TABLE IF EXISTS " + CastEntry.TABLE_NAME;

    // Movie Reviews Table
    private static final String SQL_CREATE_REVIEWS_TABLE =
            "CREATE TABLE " + ReviewsEntry.TABLE_NAME       + " ("                                      +
                    ReviewsEntry._ID                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                    ReviewsEntry.COLUMN_MOVIE_ID            + " INTEGER NOT NULL, "                     +
                    ReviewsEntry.COLUMN_AUTHOR              + " TEXT, "                                 +
                    ReviewsEntry.COLUMN_CONTENT             + " TEXT);";

    private static final String SQL_DELETE_REVIEWS_ENTRIES =
            "DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME;

    // Movie Videos Table
    private static final String SQL_CREATE_VIDEOS_TABLE =
            "CREATE TABLE " + VideosEntry.TABLE_NAME        + " ("                                      +
                    VideosEntry._ID                         + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                    VideosEntry.COLUMN_MOVIE_ID             + " INTEGER NOT NULL, "                     +
                    VideosEntry.COLUMN_VIDEO_KEY            + " TEXT, "                                 +
                    VideosEntry.COLUMN_VIDEO_NAME           + " TEXT, "                                 +
                    VideosEntry.COLUMN_VIDEO_TYPE           + " TEXT);";

    private static final String SQL_DELETE_VIDEOS_ENTRIES =
            "DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME;

    // Actor Table
    private static final String SQL_CREATE_ACTORS_TABLE =
            "CREATE TABLE " + ActorsEntry.TABLE_NAME        + " ("                                      +
                    ActorsEntry._ID                         + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                    ActorsEntry.COLUMN_ACTOR_ID             + " INTEGER NOT NULL, "                     +
                    ActorsEntry.COLUMN_BIOGRAPHY            + " TEXT, "                                 +
                    ActorsEntry.COLUMN_BIRTHDAY             + " TEXT, "                                 +
                    ActorsEntry.COLUMN_DEATH_DAY            + " TEXT, "                                 +
                    ActorsEntry.COLUMN_GENDER               + " INTEGER, "                              +
                    ActorsEntry.COLUMN_NAME                 + " TEXT, "                                 +
                    ActorsEntry.COLUMN_PLACE_OF_BIRTH       + " TEXT, "                                 +
                    ActorsEntry.COLUMN_PROFILE_PATH         + " TEXT);";

    private static final String SQL_DELETE_ACTORS_ENTRIES =
            "DROP TABLE IF EXISTS " + ActorsEntry.TABLE_NAME;

    // Actor Credits Table
    private static final String SQL_CREATE_CREDITS_TABLE =
            "CREATE TABLE " + CreditsEntry.TABLE_NAME       + " ("                                      +
                    CreditsEntry._ID                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                    CreditsEntry.COLUMN_ACTOR_ID            + " INTEGER NOT NULL, "                     +
                    CreditsEntry.COLUMN_CHARACTER           + " TEXT, "                                 +
                    CreditsEntry.COLUMN_MOVIE_ID            + " INTEGER NOT NULL, "                     +
                    CreditsEntry.COLUMN_POSTER_PATH         + " TEXT, "                                 +
                    CreditsEntry.COLUMN_RELEASE_DATE        + " TEXT, "                                 +
                    CreditsEntry.COLUMN_TITLE               + " TEXT);";

    private static final String SQL_DELETE_CREDITS_ENTRIES =
            "DROP TABLE IF EXISTS " + CreditsEntry.TABLE_NAME;

    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_DETAILS_TABLE);
        db.execSQL(SQL_CREATE_CAST_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
        db.execSQL(SQL_CREATE_VIDEOS_TABLE);

        db.execSQL(SQL_CREATE_ACTORS_TABLE);
        db.execSQL(SQL_CREATE_CREDITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MOVIE_DETAILS_ENTRIES);
        db.execSQL(SQL_DELETE_CAST_ENTRIES);
        db.execSQL(SQL_DELETE_REVIEWS_ENTRIES);
        db.execSQL(SQL_DELETE_VIDEOS_ENTRIES);

        db.execSQL(SQL_DELETE_ACTORS_ENTRIES);
        db.execSQL(SQL_DELETE_CREDITS_ENTRIES);

        onCreate(db);
    }
}
