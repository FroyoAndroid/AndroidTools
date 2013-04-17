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

    private static final int CONTACT_PICKER_RESULT = 1001;
    private static ContactModule _instance;

    public static void initInstance(){
        if (_instance == null){
            _instance = new ContactModule();
        }
    }

    public static ContactModule getInstance() {
        return _instance;
    }

    public void doLaunchContactPicker(Activity act) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        act.startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }
}
