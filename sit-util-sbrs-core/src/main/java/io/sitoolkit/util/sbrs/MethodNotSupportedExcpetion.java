package io.sitoolkit.util.sbrs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MethodNotSupportedExcpetion extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MethodNotSupportedExcpetion(String message) {
    super(message);
  }

  public MethodNotSupportedExcpetion(String message, Throwable cause) {
    super(message, cause);
  }

  public MethodNotSupportedExcpetion(Throwable cause) {
    super(cause);
  }
}
