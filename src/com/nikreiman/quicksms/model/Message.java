package com.nikreiman.quicksms.model;

import java.io.Serializable;

public class Message implements Serializable {
  private String name;
  private String number;
  private String text;

  public Message(String name, String number, String text) {
    this.name = name;
    this.number = number;
    this.text = text;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
