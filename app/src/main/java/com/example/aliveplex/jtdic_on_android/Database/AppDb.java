package com.example.aliveplex.jtdic_on_android.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Aliveplex on 9/4/2560.
 */

public class AppDb extends SQLiteOpenHelper {
    public static final String DB_NAME = "appdb.db";
    public static final int DB_VERSION = 1;
    private Context context;

    public AppDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTagTable = "create table if not exists " + TagEntity.TABLE_NAME + " (" + TagEntity._ID + " integer primary key, " + TagEntity.TAG_TEXT + " text, " +
                TagEntity.TAG_DESCRIPTION + " text)";
        String createWordTagTable = "create table if not exists " + WordTagEntity.TABLE_NAME + " (" + WordTagEntity.WORD_ID + " integer, " + WordTagEntity.TAG_ID + " integer, " +
                WordTagEntity.ADDED_DATE + " text, primary key(" + WordTagEntity.WORD_ID + ", " + WordTagEntity.TAG_ID + "), foreign key(" + WordTagEntity.TAG_ID +
                ") references " + TagEntity.TABLE_NAME + "(" + TagEntity._ID + ") on delete cascade)";
        String createIndex = "create index wordId_index on " + WordTagEntity.TABLE_NAME + " (" + WordTagEntity.WORD_ID + ");" +
                " create index tagId_index on " + WordTagEntity.TABLE_NAME + " (" + WordTagEntity.TAG_ID + ");";
        db.execSQL(createTagTable);
        db.execSQL(createWordTagTable);
        db.execSQL(createIndex);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // fucking bad practice to drop tables that stored user data
        String dropTable = "drop table if exists tags; drop table if exists wordtags;";
        String dropIndex = "drop index if exists wordId_index; drop index if exists tagId_index;";
        db.execSQL(dropTable);
        db.execSQL(dropIndex);
        onCreate(db);
    }

    public static class TagEntity implements BaseColumns {
        public static final String TABLE_NAME = "tags";
        public static final String TAG_TEXT = "tag_text";
        public static final String TAG_DESCRIPTION = "tag_description";
    }


    public static class WordTagEntity {
        public static final String TABLE_NAME = "wordtags";
        public static final String WORD_ID = "wordId";
        public static final String TAG_ID = "tagId";
        public static final String ADDED_DATE = "added_date";
    }
}
