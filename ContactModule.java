package com.soi.modules;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;

/**
 * Created by IntelliJ IDEA.
 * Date: 4/18/13
 * Time: 12:44 AM
 * Author: spirosoikonomakis
 * To change this template use File | Settings | File Templates.
 */
public class ContactModule {

    /* We use this variable as an identifier in order to use the results to the current
     * activity which we use the Contact picker
     */
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static ContactModule _instance;

    /**
     * Singleton pattern for static methods and variables
     * You must run the singletons which you will use in the first Activity
     */
    public static void initInstance(){
        if (_instance == null){
            _instance = new ContactModule();
        }
    }

    public static ContactModule getInstance() {
        return _instance;
    }

    /**
     * Shows a contact picker to choose a contact from our index.
     * @param act is the current activity eg. SoiActivity.this
     */
    public void showContactPicker(Activity act) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        act.startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    /**
     *
     * @return The contact picker identifier
     */
    public int getContactPickerResult() {
        return CONTACT_PICKER_RESULT;
    }
}
