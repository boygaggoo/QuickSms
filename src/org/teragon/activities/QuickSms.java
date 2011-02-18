package org.teragon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;
import org.teragon.controllers.MessagesController;
import org.teragon.controllers.SmsController;
import org.teragon.dialogs.AddNewMessageDialog;
import org.teragon.model.Message;
import org.teragon.quicksms.R;
import org.teragon.views.MessageListItem;

public class QuickSms extends Activity implements SmsController.Observer, View.OnClickListener, AddNewMessageDialog.Observer {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    populateMessageList();
  }

  private void populateMessageList() {
    TableLayout messagesTable = (TableLayout)getWindow().findViewById(R.id.MessagesTable);
    messagesTable.removeAllViews();

    MessagesController messagesController = new MessagesController(this);
    for(Message message : messagesController.getMessages()) {
      messagesTable.addView(new MessageListItem(this, message, this));
    }

    Button addNewMessageButton = new Button(this);
    addNewMessageButton.setText("Add New Message");
    addNewMessageButton.setOnClickListener(this);
    messagesTable.addView(addNewMessageButton, new TableLayout.LayoutParams(
      TableLayout.LayoutParams.FILL_PARENT,
      TableLayout.LayoutParams.FILL_PARENT));
  }

  public void messageSent() {
    Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();
  }

  public void messageFailure(String message) {
    Toast.makeText(this, "SMS not delivered!", Toast.LENGTH_SHORT).show();
  }

  public void onClick(View view) {
    AddNewMessageDialog dialog = new AddNewMessageDialog(this, this);
    dialog.show();
  }

  public void messageAdded() {
    populateMessageList();
  }
}
