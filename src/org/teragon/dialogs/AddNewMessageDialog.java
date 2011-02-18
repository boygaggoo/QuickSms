package org.teragon.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.teragon.controllers.MessagesController;
import org.teragon.filters.SmsLengthInputFilter;
import org.teragon.model.Contact;
import org.teragon.model.Message;
import org.teragon.quicksms.R;

public class AddNewMessageDialog extends Dialog implements View.OnClickListener, SmsLengthInputFilter.Observer {
  private Context context;
  private Contact contact;
  private Observer observer;
  private TextView messageTextLabel;
  private int messageTextDefaultColor;

  public interface Observer {
    public void messageAdded();
  }

  public AddNewMessageDialog(Context context, Contact contact, Observer observer) {
    super(context);
    this.context = context;
    this.contact = contact;
    this.observer = observer;

    setContentView(R.layout.add_new_message_dialog);
    setTitle("Add New Message");
    setCancelable(true);

    TextView contactLabel = (TextView)getWindow().findViewById(R.id.AddNewMessageContactLabel);
    contactLabel.setText("Contact: " + contact.getName() + " (" + contact.getNumber() + ")");

    EditText messageEditText = (EditText)getWindow().findViewById(R.id.AddNewMessageTextEditText);
    messageEditText.getText().setFilters(new InputFilter[] {new SmsLengthInputFilter(this)});
    messageTextLabel = (TextView)getWindow().findViewById(R.id.AddNewMessageTextLabel);
    messageTextDefaultColor = messageTextLabel.getTextColors().getDefaultColor();

    Button okButton = (Button)getWindow().findViewById(R.id.AddNewMessageOkButton);
    okButton.setOnClickListener(this);
  }

  public void onSmsTextLengthChanged(int textLength) {
    messageTextLabel.setText("Message (" + textLength + "/160)");
    messageTextLabel.setTextColor(textLength > 160 ? Color.RED : messageTextDefaultColor);
  }

  public void onClick(View view) {
    final EditText messageEditText = (EditText)getWindow().findViewById(R.id.AddNewMessageTextEditText);
    String messageText = messageEditText.getText().toString();
    if(messageText == null || messageText.length() == 0) {
      Toast.makeText(context, "Message is empty", Toast.LENGTH_LONG).show();
      return;
    }
    else if(messageText.length() > 160) {
      Toast.makeText(context, "Message is too long", Toast.LENGTH_LONG).show();
      return;
    }

    Message message = new Message(contact.getName(), contact.getNumber(), messageText);
    MessagesController messagesController = new MessagesController(context);
    messagesController.saveMessage(message);
    observer.messageAdded();
    dismiss();
  }
}
