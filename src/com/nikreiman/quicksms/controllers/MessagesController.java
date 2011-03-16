package com.nikreiman.quicksms.controllers;

import android.content.Context;
import android.util.Log;
import com.nikreiman.quicksms.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessagesController {
  private Context context;
  private List<Message> messages;

  public MessagesController(Context context) {
    this.context = context;
    readMessages();
  }

  public void readMessages() {
    messages = new ArrayList<Message>();

    for(String messageFile : context.fileList()) {
      Message message = readMessageFromFile(messageFile);
      if(message != null) {
        messages.add(message);
      }
    }
  }

  public List<Message> getMessages() {
    return messages;
  }

  private Message readMessageFromFile(String messageFile) {
    Message result = null;

    try {
      ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput(messageFile));
      result = (Message)inputStream.readObject();
    }
    catch(IOException e) {
      Log.e("QuickSms", "Could not open message file for reading");
    }
    catch(ClassNotFoundException e) {
      Log.e("QuickSms", "Could not parse message file");
    }

    return result;
  }

  public void saveMessage(Message message) {
    try {
      String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
      ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
      outputStream.writeObject(message);
    }
    catch(IOException e) {
      Log.e("QuickSms", "Could not open message file for writing");
    }
  }
}
