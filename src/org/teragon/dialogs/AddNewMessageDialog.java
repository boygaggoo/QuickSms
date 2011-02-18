package org.teragon.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.teragon.controllers.MessagesController;
import org.teragon.model.Message;
import org.teragon.quicksms.R;

public class AddNewMessageDialog extends Dialog implements View.OnClickListener {
  private Context context;
  private Observer observer;

  public interface Observer {
    public void messageAdded();
  }

  public AddNewMessageDialog(Context context, Observer observer) {
    super(context);
    this.context = context;
    this.observer = observer;
    setContentView(R.layout.add_new_message_dialog);

    Button okButton = (Button)getWindow().findViewById(R.id.AddNewMessageOkButton);
    okButton.setOnClickListener(this);
  }

  public void onClick(View view) {
    final EditText contactNameEditText = (EditText)getWindow().findViewById(R.id.AddNewMessageContactNameEditText);
    String contactName = contactNameEditText.getText().toString();
    if(contactName == null || contactName.length() == 0) {
      Toast.makeText(context, "Contact name is empty", Toast.LENGTH_LONG).show();
      return;
    }

    final EditText contactNumberEditText = (EditText)getWindow().findViewById(R.id.AddNewMessageContactNumberEditText);
    String contactNumber = contactNumberEditText.getText().toString();
    if(contactNumber == null || contactNumber.length() == 0) {
      Toast.makeText(context, "Contact number is empty", Toast.LENGTH_LONG).show();
      return;
    }

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

    Message message = new Message(contactName, contactNumber, messageText);
    MessagesController messagesController = new MessagesController(context);
    messagesController.saveMessage(message);
    observer.messageAdded();
    dismiss();
  }
}
