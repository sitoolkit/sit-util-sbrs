package io.sitoolkit.util.sbrs;

import java.util.List;

import lombok.Data;

@Data
public class EmailObject {
  private String from;
  private List<String> to;
  private String subject;
  private String htmlMessage;
  private String textMessage;
  private String mimeType;
}
