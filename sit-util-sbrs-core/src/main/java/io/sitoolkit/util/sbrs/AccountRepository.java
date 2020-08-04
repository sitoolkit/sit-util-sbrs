package io.sitoolkit.util.sbrs;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository<T extends AccountEntity> extends CrudRepository<T, String> {}
