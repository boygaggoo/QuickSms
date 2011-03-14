package com.nikreiman.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.nikreiman.adapters.MessageListAdapter;
import com.nikreiman.controllers.SmsController;
import com.nikreiman.dialogs.AddNewMessageDialog;
import com.nikreiman.model.Contact;
import com.teragon.quicksms.R;

public class QuickSms extends Activity implements SmsController.Observer, AddNewMessageDialog.Observer {
  private static final int INTENT_REQUEST_PICK_CONTACT = 1;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    populateMessageList();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.MenuAddItem:
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, INTENT_REQUEST_PICK_CONTACT);
        return true;
      case R.id.MenuEditItem:
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void populateMessageList() {
    ListView messagesListView = (ListView)getWindow().findViewById(R.id.MessagesListView);
    MessageListAdapter listAdapter = new MessageListAdapter(this, this);
    if(listAdapter.getCount() > 0) {
      messagesListView.setAdapter(listAdapter);
    }
    else {
      messagesListView.setVisibility(View.INVISIBLE);
      // UGH, why in the hell doesn't this ID get fetched??!?
      //TextView introText = (TextView)getWindow().findViewById(R.id.MessagesIntroText);
      //introText.setVisibility(View.VISIBLE);
    }
  }

  public void messageSent() {
    Toast.makeText(this, "SMS Sent!", Toast.LENGTH_SHORT).show();
    finish();
  }

  public void messageFailure(String message) {
    Toast.makeText(this, "SMS not delivered: " + message, Toast.LENGTH_LONG).show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch(requestCode) {
      case INTENT_REQUEST_PICK_CONTACT:
        if(resultCode == RESULT_OK) {
          Contact contact = new Contact(getContentResolver(), data.getData());
          AddNewMessageDialog dialog = new AddNewMessageDialog(this, contact, this);
          dialog.show();
        }
        break;
      default:
        break;
    }
  }

  public void messageAdded() {
    populateMessageList();
  }
}
