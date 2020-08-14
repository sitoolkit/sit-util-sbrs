package io.sitoolkit.util.sbrs;

public interface AccountEntityProvider<T extends AccountEntity> {
  public Class<T> getType();
}
