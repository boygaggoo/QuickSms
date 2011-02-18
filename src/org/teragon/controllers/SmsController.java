package org.teragon.controllers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import org.teragon.model.Message;

public class SmsController {
  private static final String SMS_DELIVERED = "SmsDelivered";
  private static final String SMS_SENT = "SmsSent";

  public interface Observer {
    public void messageSent();
    public void messageFailure(String message);
  }

  public void SendMessage(Context context, Message message, final Observer observer) {
    PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_SENT), 0);
    context.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch(getResultCode()) {
          case Activity.RESULT_OK:
            Log.i("QuickSms", "Sent message");
            break;
          default:
            Log.i("QuickSms", "Could not send SMS: " + getResultData());
            break;
        }
      }
    }, new IntentFilter(SMS_SENT));

    PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_DELIVERED), 0);
    context.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch(getResultCode()) {
          case Activity.RESULT_OK:
            observer.messageSent();
            break;
          default:
            String message = "Get back result code '" + getResultCode() + "'";
            observer.messageFailure(message);
            Log.w("QuickSms", message);
            break;
        }
      }
    }, new IntentFilter(SMS_DELIVERED));

    SmsManager smsManager = SmsManager.getDefault();
    smsManager.sendTextMessage(message.getNumber(), null, message.getText(), sentIntent, deliveredIntent);
    Toast.makeText(context, "Sending message...", Toast.LENGTH_SHORT).show();
  }
}
