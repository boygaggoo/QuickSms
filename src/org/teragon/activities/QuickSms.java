package org.teragon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.teragon.controllers.MessagesController;
import org.teragon.controllers.SmsController;
import org.teragon.dialogs.AddNewMessageDialog;
import org.teragon.model.Contact;
import org.teragon.model.Message;
import org.teragon.quicksms.R;
import org.teragon.views.MessageListItem;

import java.util.ArrayList;
import java.util.List;

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
    TableLayout messagesTable = (TableLayout)getWindow().findViewById(R.id.MessagesTable);
    messagesTable.removeAllViews();

    List<MessageListItem> messageListItems = new ArrayList<MessageListItem>();
    MessagesController messagesController = new MessagesController(this);
    List<Message> allMessages = messagesController.getMessages();
    if(allMessages.size() > 0) {
      for(Message message : allMessages) {
        MessageListItem messageListItem = new MessageListItem(this, message, this);
        messagesTable.addView(messageListItem);
        messageListItems.add(messageListItem);
      }
    }
    else {
      TextView introText = new TextView(this);
      introText.setTextSize(18);
      introText.setText("Welcome to QuickSms! To create a template message, press the 'menu' button on your device.");
      messagesTable.addView(introText);
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
