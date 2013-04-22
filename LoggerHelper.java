package com.soi.modules;

import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * Date: 2/16/13
 * Time: 9:52 PM
 * Author: spirosoikonomakis
 * To change this template use File | Settings | File Templates.
 */
public class LoggerHelper {

    private String _tag = "SOIKONOMAKIS";
    private static LoggerHelper _instance ;

    public static void initInstance(){
        if (_instance == null){
            _instance = new LoggerHelper();
        }
    }
    public LoggerHelper getInstance(){
        return _instance;
    }
    public void Log(String string, String string2) {
        Log.i(string, string2);
    }

    public void Log(String string) {
        Log.i(_tag, string);
    }
}
