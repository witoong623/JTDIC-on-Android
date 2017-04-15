package com.example.aliveplex.jtdic_on_android.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Aliveplex on 8/4/2560.
 */

public class JTDicDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "jtdicdb.db";
    private String db_path;
    private Context context;

    public JTDicDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        db_path = context.getApplicationInfo().dataDir + "/databases/";
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        createDB();
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        createDB();
        return super.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private void createDB() {
        try {
            boolean isExist = checkDB();
            if (!isExist) {
                // make super class create db file because we can't create it.
                SQLiteDatabase db = super.getReadableDatabase();
                db.close();

                InputStream inStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outStream = new FileOutputStream(db_path + DATABASE_NAME);
                IOUtils.copy(inStream, outStream);
                inStream.close();
                outStream.close();
            }
        } catch (IOException ioe) {
        }
    }

    private boolean checkDB() {
        File dbFile = new File(db_path + DATABASE_NAME);
        return dbFile.exists();
    }
}
