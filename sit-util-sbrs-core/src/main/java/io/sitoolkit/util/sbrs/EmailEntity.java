package io.sitoolkit.util.sbrs;

public interface EmailEntity {

  String getId();

  String getFrom();

  String getTo();

  String getSubject();

  String getTextMessage();

  String getHtmlMessage();

  String getMimeType();

  String getSendStatus();

  void setSendStatus(String sendStatus);
}
