package com.audioboxlive.pro.managers;

import android.app.Activity;
import android.content.Context;

import com.audioboxlive.pro.models.Weather;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by spirosoikonomakis on 4/3/14.
 */
public class StorageFileManager {


    private String exampledDB = "example_db.json";

    private static StorageFileManager _instance;
  
    /**
     * Singletton Pattern
     **/
    public synchronized static StorageFileManager getInstance()
    {
        if (_instance == null) {
            _instance = new StorageFileManager();
        }
        return _instance;
    }

    /**
     * Save data to external file.
     * Storage file - android
     * @param context
     * @param jsonArray
     */
    public boolean writeData(Context context, JSONArray jsonArray) {

        try {
            FileOutputStream fOut = context.openFileOutput(this.songsDB,context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(jsonArray.toString());
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Save data to external file.
     * Storage file - android
     * @param context the current context
     * @param jsonArray json array which will be stored in file
     * @param dbName the name of the file which will be stored
     */
    public boolean writeData(Context context, JSONArray jsonArray, String dbName) {

        try {
            FileOutputStream fOut = context.openFileOutput(dbName, context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(jsonArray.toString());
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Save data to external file.
     * Storage file - android
     * @param context the current context
     * @param jsonObject json object which will be stored in file
     * @param dbName the name of the file which will be stored
     */
    public boolean writeData(Context context, JSONObject jsonObject, String dbName) {

        try {
            FileOutputStream fOut = context.openFileOutput(dbName, context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(jsonObject.toString());
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Returns data in specific file as
     * JSONArray
     * @param act
     * @param dbName
     * @return
     */
    public JSONArray getData( Activity act, String dbName ) {
        String jsonString = null;
        InputStream inputStream = null;
        JSONArray data = null;

        if (act.getFileStreamPath(dbName).exists()) {
            data = new JSONArray();

            try {
                inputStream = act.openFileInput(dbName);
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                ByteArrayBuffer baf = new ByteArrayBuffer(20);

                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                jsonString = new String(baf.toByteArray());
                data = new JSONArray(jsonString);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     *
     * @param act
     * @param dbName
     * @return
     */
    public boolean clearData(Activity act, String dbName)
    {
        try {
            FileOutputStream fOut = act.openFileOutput(dbName,act.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(new JSONArray().toString());
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }
}
