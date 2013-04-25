package com.soi.modules;

import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * Date: 2/16/13
 * Time: 9:52 PM
 * Author: spirosoikonomakis
 * To change this template use File | Settings | File Templates.
 */
public class LoggerWrapper {

    private String _tag = "SOIKONOMAKIS";
    private static LoggerWrapper _instance ;
    public int LOGGER_LEVEL = Log.INFO;

    public LoggerWrapper(int LOGGER_LEVEL, String tag) {
        this.LOGGER_LEVEL = LOGGER_LEVEL;
        this._tag = tag;
    }

    public static void initInstance(int LOGGER_LEVEL, String tag){
        if (_instance == null){
            _instance = new LoggerWrapper(LOGGER_LEVEL,tag);
        }
    }
    public static LoggerWrapper getInstance(){
        return _instance;
    }

    /**
     * Logs with [INFO][tag] msg
     * @param tag The tag which the logger will shown in logcat
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param args the arguments which you want to use
     */
    public void logInfo(String tag, String msgFormat, Object...args) {
        Log.i(tag, String.format(msgFormat,args));
    }
    /**
     * Logs with [INFO][_tag] msg
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param args the arguments which you want to use
     */
    public void logInfo(String msgFormat, Object...args) {
        Log.i(_tag, String.format(msgFormat,args));
    }

    /**
     * Logs with [DEBUG][tag] msg
     * @param tag The tag which the logger will shown in logcat
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param args the arguments which you want to use
     */
    public void logDebug(String tag, String msgFormat, Object...args) {
        if(LOGGER_LEVEL <= Log.DEBUG)
            Log.d(tag, String.format(msgFormat,args));
    }
    /**
     * Logs with [DEBUG][_tag] msg
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param args the arguments which you want to use
     */
    public void logDebug(String msgFormat, Object...args) {
        if(LOGGER_LEVEL <= Log.WARN)
            Log.d(_tag,  String.format(msgFormat,args));
    }
    /**
     * Logs with [ERROR][tag] msg exception
     * @param tag The tag which the logger will shown in logcat
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param ex the exception which occurred
     * @param args the arguments which you want to use
     */
    public void logError(String tag, String msgFormat, Exception ex, Object...args) {
        Log.e(tag, String.format(msgFormat,args),ex);
    }
    /**
     * Logs with [ERROR][tag] msg exception
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param ex the exception which occurred
     * @param args the arguments which you want to use
     */
    public void logError(String msgFormat, Exception ex, Object...args) {
        Log.e(_tag, String.format(msgFormat,args), ex);
    }
    /**
     * Logs with [ERROR][tag] msg exception
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param t the throwable which occurred
     * @param args the arguments which you want to use
     */
    public void logError(String msgFormat, Throwable t, Object...args) {
        Log.e(_tag, String.format(msgFormat,args), t);
    }
    /**
     * Logs with [ERROR][tag] msg exception
     * @param tag The tag which the logger will shown in logcat
     * @param tag The tag which the logger will shown in logcat
     * @param msgFormat the format the message will be appeared "Hello %s, isn't %s cool?
     * @param t the exception which occurred
     * @param args the arguments which you want to use
     */
    public void logError(String tag, String msgFormat, Throwable t, Object...args) {
        Log.e(tag, String.format(msgFormat,args),t);
    }



}
