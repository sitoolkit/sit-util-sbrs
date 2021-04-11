package io.sitoolkit.util.sbrs;

public interface EmailEntity {

  String getId();

  String getTo();

  String getSubject();

  String getMessage();

  String getSendStatus();
  
  void setSendStatus(String sendStatus);
}
