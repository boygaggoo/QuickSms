package com.nikreiman.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nikreiman.controllers.SmsController;
import com.nikreiman.model.Message;
import com.nikreiman.controllers.MessagesController;
import com.teragon.quicksms.R;

public class MessageListAdapter extends BaseAdapter {
  private MessagesController messagesController;
  private LayoutInflater inflater;
  private Context context;
  private SmsController.Observer observer;

  public MessageListAdapter(Context context, SmsController.Observer observer) {
    this.context = context;
    this.observer = observer;

    messagesController = new MessagesController(context);
    inflater = LayoutInflater.from(context);
  }

  public int getCount() {
    return messagesController.getMessages().size();
  }

  public Object getItem(int index) {
    return messagesController.getMessages().get(index);
  }

  public long getItemId(int index) {
    return index;
  }

  public View getView(int index, View view, ViewGroup parentView) {
    RelativeLayout layout = (RelativeLayout)view;

    if(layout == null) {
      layout = (RelativeLayout)inflater.inflate(R.layout.message_list_item, parentView, false);
    }

    final Message message = (Message)getItem(index);
    TextView messageTextView = (TextView)layout.findViewById(R.id.MessageListItemMessage);
    messageTextView.setText(message.getText());

    TextView contactTextview = (TextView)layout.findViewById(R.id.MessageListItemContact);
    contactTextview.setText(message.getName() + " (" + message.getNumber() + ")");
    layout.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        SmsController smsController = new SmsController(context, observer);
        smsController.SendMessage(message);
      }
    });

    return layout;
  }
}
