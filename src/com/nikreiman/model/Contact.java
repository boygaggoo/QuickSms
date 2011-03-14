package com.nikreiman.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class Contact {
  private String name;
  private String number;

  public Contact(ContentResolver contentResolver, Uri contactUri) {
    long contactId = -1;

    // Load the display name for the specified person
    final String[] nameProjection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
    Cursor cursor = contentResolver.query(contactUri, nameProjection, null, null, null);
    try {
      if(cursor.moveToFirst()) {
        contactId = cursor.getLong(0);
        setName(cursor.getString(1));
      }
    }
    finally {
      cursor.close();
    }

    // Load the phone number (if any).
    final String[] numberProjection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
    final String numberSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId;
    final String numberSortOrder = ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY + " DESC";
    cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
      numberProjection, numberSelection, null, numberSortOrder);
    try {
      if(cursor.moveToFirst()) {
        setNumber(cursor.getString(0));
      }
    }
    finally {
      cursor.close();
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }
}
