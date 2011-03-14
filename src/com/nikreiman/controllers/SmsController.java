package com.nikreiman.controllers;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import com.nikreiman.model.Message;

import java.util.Timer;
import java.util.TimerTask;

public class SmsController {
  private BroadcastReceiver sentReceiver;
  private BroadcastReceiver deliveredReceiver;
  private Context context;

  public interface Observer {
    public void messageSent();
    public void messageFailure(String message);
  }

  private static final String SMS_DELIVERED = "SmsDelivered";
  private static final String SMS_SENT = "SmsSent";
  private static final long SMS_FAIL_TIMEOUT_IN_MS = 10 * 1000;

  private Observer observer;
  private ProgressDialog progressDialog;
  private Timer failTimer;

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(android.os.Message msg) {
      messageFailed(msg.getData().getString("message"));
    }
  };

  public SmsController(Context context, Observer observer) {
    this.context = context;
    this.observer = observer;
  }

  public void SendMessage(Message message) {
    PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_SENT), 0);
    sentReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
          case Activity.RESULT_OK:
            Log.i("QuickSms", "Sent message");
            break;
          default:
            messageFailed(getResultData());
            break;
        }
      }
    };
    context.registerReceiver(sentReceiver, new IntentFilter(SMS_SENT));

    PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_DELIVERED), 0);
    deliveredReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
          case Activity.RESULT_OK:
            messageSent();
            break;
          default:
            String message = "Get result code '" + getResultCode() + "'";
            messageFailed(message);
            break;
        }
      }
    };
    context.registerReceiver(deliveredReceiver, new IntentFilter(SMS_DELIVERED));

    SmsManager smsManager = SmsManager.getDefault();
    smsManager.sendTextMessage(message.getNumber(), null, message.getText(), sentIntent, deliveredIntent);

    failTimer = new Timer();
    failTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        android.os.Message handlerMessage = new android.os.Message();
        Bundle bundle = new Bundle();
        bundle.putString("message", "Timed out");
        handlerMessage.setData(bundle);
        handler.sendMessage(handlerMessage);
      }
    }, SMS_FAIL_TIMEOUT_IN_MS);

    progressDialog = ProgressDialog.show(context, null, "Sending message...", true, true);
    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      public void onCancel(DialogInterface dialogInterface) {
        messageFailed("Cancelled");
      }
    });
  }

  private void messageSent() {
    cleanUp();
    observer.messageSent();
  }

  private void messageFailed(String message) {
    cleanUp();
    observer.messageFailure(message);
    Log.w("QuickSms", "Failed sending SMS: " + message);
  }

  private void cleanUp() {
    failTimer.cancel();
    failTimer.purge();
    progressDialog.dismiss();
    context.unregisterReceiver(sentReceiver);
    context.unregisterReceiver(deliveredReceiver);
  }
}
