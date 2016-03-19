package com.github.lazylibrary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import java.io.InputStreamReader;

/**
 * AssetDatabaseOpenHelper
 * <ul>
 * <li>Auto copy databse form assets to /data/data/package_name/databases</li>
 * <li>You can use it like {@link SQLiteDatabase}, use {@link #getWritableDatabase()} to create and/or open a database
 * that will be used for reading and writing. use {@link #getReadableDatabase()} to create and/or open a database that
 * will be used for reading only.</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-5
 */
public class AssetDatabaseOpenHelper {

    private Context context;
    private String  databaseName;

    public AssetDatabaseOpenHelper(Context context, String databaseName) {
        this.context = context;
        this.databaseName = databaseName;
    }

    /**
     * Create and/or open a database that will be used for reading and writing.
     * 
     * @return  返货数据库的对象
     * @throws RuntimeException if cannot copy database from assets
     * @throws SQLiteException if the database cannot be opened
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        File dbFile = context.getDatabasePath(databaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Create and/or open a database that will be used for reading only.
     * 
     * @return  返回读的数据库对象
     * @throws RuntimeException if cannot copy database from assets
     * @throws SQLiteException if the database cannot be opened
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        File dbFile = context.getDatabasePath(databaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * @return the database name
     */
    public String getDatabaseName() {
        return databaseName;
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream stream = context.getAssets().open(databaseName);
        FileUtils.writeFile(dbFile, stream);
        stream.close();
    }
    /**
     * 获取asset文件下的资源文件信息
     * @param fileName
     * @return
     */
    public static String getFromAssets(String fileName,Context context) {

        try {
            InputStreamReader inputReader = new InputStreamReader(
                    context.getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
