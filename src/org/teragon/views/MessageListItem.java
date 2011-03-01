package org.teragon.views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.teragon.controllers.SmsController;
import org.teragon.model.Message;

public class MessageListItem extends TableRow implements View.OnClickListener {
  private static final int PADDING = 6;
  private static final float TEXT_SIZE_LARGE = 16.0f;
  private static final float TEXT_SIZE_SMALL = 12.0f;

  private Context context;
  private Message message;
  private SmsController.Observer observer;

  public MessageListItem(Context context, Message message, SmsController.Observer observer) {
    super(context);

    this.context = context;
    this.message = message;
    this.observer = observer;

    setBackgroundColor(Color.DKGRAY);

    addMessageViews(context, message);
    setOnClickListener(this);
  }

  public void addMessageViews(Context context, Message message) {
    TableLayout tableLayout = new TableLayout(context);
    tableLayout.setPadding(PADDING, PADDING, PADDING, PADDING);

    TableRow messageRow = new TableRow(context);
    TextView messageText = new TextView(context);
    messageText.setTextSize(TEXT_SIZE_LARGE);
    messageText.setText(message.getText());
    messageRow.addView(messageText);
    tableLayout.addView(messageRow);

    TableRow detailsRow = new TableRow(context);
    TextView detailsText = new TextView(context);
    detailsText.setTextSize(TEXT_SIZE_SMALL);
    detailsText.setText(message.getName() + " (" + message.getNumber() + ")");
    detailsRow.addView(detailsText);
    tableLayout.addView(detailsRow);

    addView(tableLayout);
  }

  public void onClick(View view) {
    SmsController smsController = new SmsController(context, observer);
    smsController.SendMessage(message);
  }
}
