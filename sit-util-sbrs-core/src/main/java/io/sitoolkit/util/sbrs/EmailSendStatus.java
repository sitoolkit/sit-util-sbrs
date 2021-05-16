package io.sitoolkit.util.sbrs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmailSendStatus {
  UNSENT("0", "未送信"), 
  SENT("1", "送信済み"), 
  FAILURE("2", "送信失敗");

  @Getter private String value;

  @Getter private String label;
}