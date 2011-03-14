package com.nikreiman.filters;

import android.text.InputFilter;
import android.text.Spanned;

public class SmsLengthInputFilter implements InputFilter {
  private Observer observer;

  public interface Observer {
    public void onSmsTextLengthChanged(int textLength);
  }

  public SmsLengthInputFilter(Observer observer) {
    this.observer = observer;
  }

  public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd,
                             Spanned dest, int destStart, int destEnd) {
    observer.onSmsTextLengthChanged(dest.length());
    return source;
  }
}
